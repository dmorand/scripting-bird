package scriptingbird.language

object Clojure extends ScriptingLanguage {
  val regex = "#clojure (.*)".r

  def eval(expression: String): AnyRef = {
    return "I don't speak clojure yet"
  }
}