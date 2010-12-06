import java.lang.Thread._

import scala.collection.JavaConversions._
import scala.xml._

import twitter4j._
import twitter4j.http._

import language._

object ScriptingBird {
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
      case Scala.regex(expression) => Scala.eval(expression)
      case Groovy.regex(expression) => Groovy.eval(expression)
      case Python.regex(expression) => Python.eval(expression)
      case Ruby.regex(expression) => Ruby.eval(expression)
      case _ => "Unable to evalute expression"
    }
  }
}