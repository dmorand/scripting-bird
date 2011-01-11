package scriptingbird

import java.lang.Thread._

import scala.collection.JavaConversions._
import scala.util.matching._
import scala.xml._

import twitter4j._
import twitter4j.http._

object ScriptingBird {
  val resetRegex = "#reset".r
  val languageRegex = "#([^ ]+)".r

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
        val value = handle(null, message.getText)
        twitter.sendDirectMessage(friendID, value.toString)
      }
    }
  }

  def consoleMain = {
    val jsr223 = new Jsr223()
    while (true) {
      println(handle(jsr223, readLine).toString)
    }
  }

  def handle(jsr223: Jsr223, message: String): AnyRef = {
    message match {
      case resetRegex() => jsr223.reset()
      case languageRegex(language) => jsr223.language(language)
      case _ => jsr223.eval(message)
    }
  }
}