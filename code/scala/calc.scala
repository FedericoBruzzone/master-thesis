class Calculator(val value: Int) {
  infix def add(x: Int): Calculator = new Calculator(value + x)
  infix def subtract(x: Int): Calculator = new Calculator(value - x)
}


@main def CalcExample =
  val calc = Calculator(0)
  val result = calc add 5 subtract 3 add 10
  println(result.value) // 12

