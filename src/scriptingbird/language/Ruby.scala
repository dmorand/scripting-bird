package scriptingbird.language

object Ruby extends ScriptingLanguage {
  val regex = "#ruby (.*)".r

  def eval(expression: String): AnyRef = {
    return "I don't speak ruby yet"
  }
}