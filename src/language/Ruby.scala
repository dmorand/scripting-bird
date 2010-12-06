package language

object Ruby extends ScriptingLanguage {
  val regex = "#ruby (.*)".r

  def eval(expression: String): AnyRef = {
    return null
  }
}