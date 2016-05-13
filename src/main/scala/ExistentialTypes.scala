import scala.language.existentials

/**
  * Existential Types -> see Scala Reference Chapter 3.2.10
  */
object ExistentialTypes extends App {
  //existential type
  //will be removed with Scala 2.12
  //doesn't exist in dotty
  def elementsHashcode(a: Array[T] forSome  {type T})= {
    a.map(_.hashCode())
  }

  //placeholder syntax for existential types
  //will remain with Scala 2.12 and dotty
  def length(a: Array[_]) = {
    a.length
  }

  //with upper bound
  def wordsLength(a: Array[_ <: CharSequence]) = {
    a.map(_.length())
  }

  def arrayAsString(a: Array[_]) = {
    a.map(_.toString).reduce(_ + ", " + _)
  }

  val s = Array[String]("abc", "def", "hijklm")

  println(arrayAsString(wordsLength(s)))
  println(arrayAsString(elementsHashcode(s)))
}
