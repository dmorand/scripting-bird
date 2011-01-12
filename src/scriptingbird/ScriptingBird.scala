package scriptingbird

import handler._

import scala.collection.JavaConversions._
import scala.util.matching._
import scala.xml._

import java.lang.Thread._

import twitter4j._
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

    val twitter = new TwitterFactory().getOAuthAuthorizedInstance(consumerKey, consumerSecret, new AccessToken(accessToken, accessTokenSecret))
    val friendIDs = twitter.getFriendsIDs.getIDs

    val messages = asScalaIterable(twitter.getDirectMessages())
    for (message <- messages) {
      val friendID = message.getSenderId;
      if (friendIDs.contains(friendID)) {
        //val value = handle(null, message.getText)
        //twitter.sendDirectMessage(friendID, value.toString)
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