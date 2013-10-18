package scriptingbird

import handler._

import scala.collection.JavaConversions._
import scala.collection.mutable.Map
import scala.language.postfixOps
import scala.util.matching._
import scala.xml._

import java.lang.Thread.sleep
import java.lang.System.currentTimeMillis

import twitter4j._
import twitter4j.conf._;
import twitter4j.http._

object ScriptingBird {
  val changeLanguageRegex = "#([^ ]+)".r

  def main(args: Array[String]): Unit = {
    //twitterMain(args)
    consoleMain
  }

  def twitterMain(args: Array[String]): Unit = {
    val config = XML.loadFile(args(0));
    val accessToken = config \ "accessToken" text
    val accessTokenSecret = config \ "accessTokenSecret" text
    val consumerKey = config \ "consumerKey" text
    val consumerSecret = config \ "consumerSecret" text

    val twitter = new TwitterFactory().getInstance(new AccessToken(accessToken, accessTokenSecret))
    val friendIDs = twitter.getFriendsIDs.getIDs
    val friendActors = Map[Int, ScriptingBirdActor]()
    var lastDirectMessageId: Long = 0

    var messages = collectionAsScalaIterable(twitter.getDirectMessages())
    for (message <- messages) {
        lastDirectMessageId = message.getId()
    }
    
    println(lastDirectMessageId)
    
    while (true) {
      sleep(20000)
      println("Fetching direct messages...")
      messages = twitter.getDirectMessages(new Paging(lastDirectMessageId))
      println("Got "+ messages.size() + " messages...")
      for (message <- messages) {
        val friendID = message.getSenderId;
        if (friendIDs.contains(friendID)) {
          if(!friendActors.contains(friendID)) {
              friendActors += (friendID -> new ScriptingBirdActor(new TwitterHandler(twitter, friendID)))
              friendActors(friendID).start
          }
          
          friendActors(friendID) ! getMessage(message.getText) 
          lastDirectMessageId = message.getId()
          println(lastDirectMessageId)
        }
      }
    }
  }

  def consoleMain = {
    val actor = new ScriptingBirdActor(ConsoleHandler)
    actor.start

    while (true) {
      actor ! getMessage(readLine)
    }
  }

  def getMessage(text: String): Message = {
    text match {
      case "#reset" => Reset()
      case "#languages" => AvailableLanguages()
      case changeLanguageRegex(language) => ChangeLanguage(language)
      case _ => EvaluateExpression(text)
    }
  }
}