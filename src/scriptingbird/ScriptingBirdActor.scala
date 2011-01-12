package scriptingbird

import handler._

import scala.actors._
import scala.collection.JavaConversions._
import scala.collection.mutable.Map

import javax.script._

class ScriptingBirdActor(handler: ScriptHandler) extends Actor {
  private var scriptEngineManager = new ScriptEngineManager()
  private var scriptEngine: ScriptEngine = null

  def act {
    loop {
      receive {
        case ChangeLanguage(language) => handleResult(changeLanguage(language))
        case EvaluateExpression(expression) => handleResult(evaluateExpression(expression))
        case AvailableLanguages() => handleResult(availableLanguages())
        case Reset() => handleResult(reset())
      }
    }
  }

  def changeLanguage(language: String): String = {
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

  def evaluateExpression(expression: String): String = {
    if (scriptEngine == null) {
      return "No Script Engine Defined"
    }

    try {
      scriptEngine.eval(expression).toString()
    } catch {
      case exception: Exception => exception.getMessage()
      case error: Error => error.getMessage()
    }
  }

  def reset(): String = {
    scriptEngineManager = new ScriptEngineManager()
    scriptEngine = null
    "Resetted"
  }

  def availableLanguages(): String = {
    scriptEngineManager.getEngineFactories().map(e => e.getLanguageName).mkString(",")
  }

  def handleResult(result: String) {
    handler.handle(result)
  }
}