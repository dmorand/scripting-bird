import java.lang.Thread._

import scala.collection.JavaConversions._
import scala.tools.nsc._
import scala.xml._

import twitter4j._
import twitter4j.http._

object ScriptingBird {
  def main(args: Array[String]): Unit = {
    val config = XML.loadFile(args(0));
    val accessToken = config \ "accessToken" text
    val accessTokenSecret = config \ "accessTokenSecret" text
    val consumerKey = config \ "consumerKey" text
    val consumerSecret = config \ "consumerSecret" text

    val interpreterSettings = new Settings()
    interpreterSettings.usejavacp.value = true
    val interpreter = new scala.tools.nsc.Interpreter(interpreterSettings)
    val twitter = new TwitterFactory().getOAuthAuthorizedInstance(consumerKey, consumerSecret, new AccessToken(accessToken, accessTokenSecret))
    val friendIDs = twitter.getFriendsIDs.getIDs

    val messages = asScalaIterable(twitter.getDirectMessages())
    for (message <- messages) {
      val friendID = message.getSenderId;
      if (friendIDs.contains(friendID)) {
        val value = interpreter.evalExpr[AnyRef](message.getText.replaceFirst("#scala ", ""))
        twitter.sendDirectMessage(friendID, value.toString)
      }
    }
  }
}