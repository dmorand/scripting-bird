package language

import scala.util.matching._

abstract class ScriptingLanguage {
  val regex: Regex
  
  def eval(expression: String)
}