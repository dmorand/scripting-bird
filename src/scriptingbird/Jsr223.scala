package scriptingbird

import scala.collection.JavaConversions._
import scala.collection.mutable.Map

import javax.script._

class Jsr223 /*extends Actor*/ {
  private var scriptEngineManager = new ScriptEngineManager()
  private var scriptEngine: ScriptEngine = null

  def language(language: String): String = {

    if (scriptEngine != null) {
      scriptEngineManager.setBindings(scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE));
    }

    scriptEngine = scriptEngineManager.getEngineByName(language);

    if (scriptEngine != null) {
      val factory = scriptEngine.getFactory()
      "Using " + factory.getLanguageName + " " + factory.getLanguageVersion
    } else
      "Unknown scripting language: '" + language + "'"
  }

  def reset(): String = {
    scriptEngineManager = new ScriptEngineManager()
    scriptEngine = null
    "Resetted"
  }

  def eval(expression: String): AnyRef = {
    if (scriptEngine == null) {
      return "No Script Engine Defined"
    }

    try {
      scriptEngine.eval(expression)
    } catch {
      case ex => ex.getMessage()
    }
  }

  def printAvailableLanguages = {
    scriptEngineManager.getEngineFactories().foreach(println)
  }
}