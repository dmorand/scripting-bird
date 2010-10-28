import org.apache.commons.httpclient._, auth._, methods._, params._
import scala.xml._
import java.io._
import java.nio._

object ScriptingBird {
  def main(args: Array[String]): Unit = {
    println("Password: ")
    val password = new BufferedReader(new InputStreamReader(System.in)).readLine()
    println(new ScriptingBird(password).getMentions)
  }
}

class ScriptingBird(password: String) {
  def getMentions: String = {
    val client = new HttpClient()
    val method = new GetMethod("http://twitter.com/statuses/mentions.xml")
    val credentials = new UsernamePasswordCredentials("scriptingbird", password)

    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false))
    client.getParams().setAuthenticationPreemptive(true)
    client.getState().setCredentials(new AuthScope("twitter.com", 80, AuthScope.ANY_REALM), credentials)
    client.executeMethod(method)
    method.getResponseBodyAsString()
  }
}