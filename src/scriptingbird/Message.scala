package scriptingbird

abstract class Message
case class Reset() extends Message
case class AvailableLanguages() extends Message
case class ChangeLanguage(language: String) extends Message
case class EvaluateExpression(expression: String) extends Message