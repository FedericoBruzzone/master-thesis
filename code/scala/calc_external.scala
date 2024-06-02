import scala.util.parsing.combinator._

class Calculator(val value: Int) {
  def add(x: Int): Calculator      = new Calculator(value + x)
  def subtract(x: Int): Calculator = new Calculator(value - x)
  def multiply(x: Int): Calculator = new Calculator(value * x)
  def divide(x: Int): Calculator   = new Calculator(value / x)
}

object CalcParser extends JavaTokenParsers {
  def number: Parser[Int] = wholeNumber ^^ { _.toInt }

  def factor: Parser[Calculator => Calculator] = (
    "add"      ~> number ^^ { x => _.add(x) }
  | "subtract" ~> number ^^ { x => _.subtract(x) }
  | "multiply" ~> number ^^ { x => _.multiply(x) }
  | "divide"   ~> number ^^ { x => _.divide(x) }
  )

  def expression: Parser[Calculator => Calculator] = rep(factor) ^^ {
    _.reduceLeft { (acc, f) => acc andThen f }
  }

  def parseExpression(input: String): Option[Calculator => Calculator] = (
    parseAll(expression, input) match {
      case Success(result, _) => Some(result)
      case _                  => None
    }
  )
}

@main def CalcExample =
  val calc = new Calculator(0)
  val expression = "add 5 multiply 2 subtract 1 divide 2"
  val resultFunction = CalcParser.parseExpression(expression)

  resultFunction match {
    case Some(f) => println(f(calc).
    case None    => println("Error parsing expression")
  }

