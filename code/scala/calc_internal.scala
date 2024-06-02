class Calculator(val value: Int) {
  infix def add(x: Int): Calculator = new Calculator(value + x)
  infix def subtract(x: Int): Calculator = new Calculator(value - x)
  infix def multiply(x: Int): Calculator = new Calculator(value * x)
  infix def divide(x: Int): Calculator = new Calculator(value / x)
}

@main def CalcExample =
  val calc = Calculator(0)
  val result = calc add 5 multiply 2 subtract 1 divide 2
  println(result.value)

