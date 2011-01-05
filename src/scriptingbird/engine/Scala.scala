package scriptingbird.engine

import scala.tools.nsc._

object Scala {
  private val interpreterSettings = new Settings()
  interpreterSettings.usejavacp.value = true
  private val interpreter = new Interpreter(interpreterSettings)

  def handles(language: String): Boolean = {
      "scala".equalsIgnoreCase(language)
  }
  
  def eval(expression: String): AnyRef = {
    interpreter.evalExpr[AnyRef](expression)
  }
}