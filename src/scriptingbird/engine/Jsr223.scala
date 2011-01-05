package scriptingbird.engine

import scala.collection.JavaConversions._

import javax.script._ 

object Jsr223 {
    private val scriptEngineManager = new ScriptEngineManager()
    
    def handles(language: String): Boolean = {
      scriptEngineManager.getEngineByName(language) != null                                           
    }
    
    def eval(language: String, expression: String): AnyRef = {
      scriptEngineManager.getEngineByName(language).eval(expression)
    }
    
    def printAvailableLanguages = {
        scriptEngineManager.getEngineFactories().foreach(println)
    }
}