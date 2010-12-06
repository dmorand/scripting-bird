package language

import scala.tools.nsc._

object Scala extends ScriptingLanguage {
  val regex = "#scala (.*)".r

  private val interpreterSettings = new Settings()
  interpreterSettings.usejavacp.value = true
  private val interpreter = new Interpreter(interpreterSettings)

  def eval(expression: String): AnyRef = {
    interpreter.evalExpr[AnyRef](expression)
  }
}