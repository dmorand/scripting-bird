package scriptingbird.handler

object ConsoleHandler extends ScriptHandler {
  def handle(result: String) {
      println(result)
  }
}