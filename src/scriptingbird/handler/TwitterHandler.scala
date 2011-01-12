package scriptingbird.handler

import java.lang.Math._

import twitter4j._

class TwitterHandler(twitter: Twitter, friendID: Int) extends ScriptHandler {
  def handle(result: String) {
    val message = result.substring(0, min(result.length, 140))
    twitter.sendDirectMessage(friendID, message)
    println(message)
  }
}