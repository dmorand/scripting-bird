package language

object Groovy  extends ScriptingLanguage {
    val regex = "#groovy (.*)".r

    def eval(expression: String) = {
        
    }
}