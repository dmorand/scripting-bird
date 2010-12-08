package scriptingbird.language

object Ecmascript extends ScriptingLanguage {
  val regex = "#ecmascript (.*)".r

  def eval(expression: String): AnyRef = {
    return "I don't speak ecmascript yet"
  }
}