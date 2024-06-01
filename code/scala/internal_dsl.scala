// // Define a basic structure for an HTML element
// sealed trait HtmlElement:
//   def render: String
//
// case class TextElement(text: String) extends HtmlElement:
//   def render: String = text
//
// case class HtmlTag(tagName: String, children: HtmlElement*) extends HtmlElement:
//   def render: String =
//     s"<$tagName>${children.map(_.render).mkString}</$tagName>"
//
// // Helper object to create HTML elements
// object Html:
//   def text(text: String): HtmlElement = TextElement(text)
//   def tag(name: String, children: HtmlElement*): HtmlElement = HtmlTag(name, children: _*)
//   def div(children: HtmlElement*): HtmlElement = tag("div", children: _*)
//   def p(children: HtmlElement*): HtmlElement = tag("p", children: _*)
//   def span(children: HtmlElement*): HtmlElement = tag("span", children: _*)
//
//   // Add more elements as needed
//
// // Example usage of the HTML DSL
// @main def HtmlDslExample =
//   import Html._
//
//   val htmlContent = div(
//     p(
//       text("This is a paragraph with "),
//       span(text("some inline text"))
//     ),
//     div(
//       text("This is a nested div.")
//     )
//   )
//
//   println(htmlContent.render)


// Class version
// Define a basic structure for an HTML element
sealed trait HtmlElement:
  def render: String

case class TextElement(text: String) extends HtmlElement:
  def render: String = text

case class HtmlTag(tagName: String, children: HtmlElement*) extends HtmlElement:
  def render: String =
    s"<$tagName>${children.map(_.render).mkString}</$tagName>"

// Helper object to create HTML elements
class Html:
  infix def text(text: String): HtmlElement = TextElement(text)
  infix def tag(name: String, children: HtmlElement*): HtmlElement = HtmlTag(name, children: _*)
  infix def div(children: HtmlElement*): HtmlElement = tag("div", children: _*)
  infix def p(children: HtmlElement*): HtmlElement = tag("p", children: _*)
  infix def span(children: HtmlElement*): HtmlElement = tag("span", children: _*)

  // Add more elements as needed

// Example usage of the HTML DSL
@main def HtmlDslExample =
  var html = new Html
  val htmlContent = html.div(
    html.p(
      html.text("This is a paragraph with "),
      html.span(html.text("some inline text"))
    ),
    html.div(
      html.text("This is a nested div.")
    )
  )

  println(htmlContent.render)

