import scala.language.reflectiveCalls

/**
  * Using a structural type for printing, see Scala Spec 3.2.7
  * and https://dzone.com/articles/duck-typing-scala-structural
  *
  * Function calls require reflection, therefore the reflectiveCalls language feature
  * has to be enabled
  */
object StructuralTypes extends App {
  class A() {
    //x cannot be final
    val x = 1

    def y(what: String = "a") = what
  }

  class B() extends A() {
    override val x = 2

    override def y(what: String) = "B_" + what
    def z() = "b"
  }

  case class C(x: Int = 23, t: String = "default") {
    def y(something: String) = "something from Class C:" + something
    def z() =  "c"
  }

  object Printer {
    type Printable = {val x: Int; def y(what: String): String; def z(): String}
    def print(printable: Printable): Unit = {
      println(" x:" + printable.x + " y:" + printable.y("callValue") + " z:" + printable.z())
    }
  }

  val a = new A()
  val ba = new B()
  val c = C()

  //will not compile, because a does not match the structural type
  //Printer(a)
  Printer.print(ba)
  Printer.print(c)
}
