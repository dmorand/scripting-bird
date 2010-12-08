package scriptingbird.language

object Python extends ScriptingLanguage {
  val regex = "#python (.*)".r

  def eval(expression: String): AnyRef = {
    return "I don't speak python yet"
  }
}