package scriptingbird.language

import groovy.lang._

object Groovy  extends ScriptingLanguage {
    val regex = "#groovy (.*)".r
    private val shell = new GroovyShell(new Binding())
    
    def eval(expression: String): AnyRef = {
        shell.evaluate(expression)
    }
}