package scriptingbird

import java.lang.Thread._

import scala.collection.JavaConversions._
import scala.xml._
import scala.util.matching._

import twitter4j._
import twitter4j.http._

import engine._

object ScriptingBird {    
  val messageRegex = "#([^ ]+) (.*)".r

  def main(args: Array[String]): Unit = {
    Jsr223.printAvailableLanguages
    //twitterMain(args)
    consoleMain
  }

  def twitterMain(args: Array[String]): Unit = {
    val config = XML.loadFile(args(0));
    val accessToken = config \ "accessToken" text
    val accessTokenSecret = config \ "accessTokenSecret" text
    val consumerKey = config \ "consumerKey" text
    val consumerSecret = config \ "consumerSecret" text

    val twitter = new TwitterFactory().getOAuthAuthorizedInstance(consumerKey, consumerSecret, new AccessToken(accessToken, accessTokenSecret))
    val friendIDs = twitter.getFriendsIDs.getIDs

    val messages = asScalaIterable(twitter.getDirectMessages())
    for (message <- messages) {
      val friendID = message.getSenderId;
      if (friendIDs.contains(friendID)) {
        val value = eval(message.getText)
        twitter.sendDirectMessage(friendID, value.toString)
      }
    }
  }

  def consoleMain = {
    while (true) {
      println(eval(readLine).toString)
    }
  }

  def eval(message: String): AnyRef = {
    message match {
      case messageRegex(language, expression) => eval(language, expression)
      case _ => "Invalid message format"
    }
  }
  
  def eval(language: String, expression: String): AnyRef = {
    if (Jsr223.handles(language))
      Jsr223.eval(language, expression)
    else if (Scala.handles(language))
      Scala.eval(expression)         
    else 
      "Unknown scripting language '" + language + "'"
  }
}