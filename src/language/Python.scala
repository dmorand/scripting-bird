package language

object Python extends ScriptingLanguage {
  val regex = "#python (.*)".r

  def eval(expression: String): AnyRef = {
    return null
  }
}