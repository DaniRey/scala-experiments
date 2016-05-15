/**
  * Focus on inheritance and precedence/overriding
  * see Scala Spec 5.1.2 Class Linearization
  */
object Inheritance extends App {

  class A() {
    //x cannot be final
    val x = 1

    def y(what: String = "a") = what
  }

  class B() extends A() {
    override val x = 2

    //overriding function without loosing the inherited default value
    override def y(what: String) = "B_" + what
    def z() = "b"
  }

  trait C {
    //is abstract, no override is needed
    val x: Int
  }

  trait D extends A {
    //must contain override keyword
    //override keyword may only exist, if it explicitly extends a class containing this field/function already
    override val x = 4
    override def y(what: String) = "D_" + what
  }

  trait E extends A {
    override val x = 5
    override def y(what: String = "eDefault" ) = "E_" + what
  }

  object Printer {
    def print(printable: B): Unit = {
      println(" x:" + printable.x + " y:" + printable.y() + " z:" + printable.z())
    }
  }

  val a = new A()
  val ba = new B()

  val cba = new B() with C
  val dba = new B() with D

  //edcba will be linearized to {B, E, D, C, A}
  //rules for overriding
  //let M and M' be the val,var,def in question, then M will override M' if
  //M appears in a class which precedes the class of M' in the linearization of edcba
  val edcba = new B() with C with D with E

  //will not compile, because a does not match the structural type
  //Printer(a)
  Printer.print(ba)
  Printer.print(cba)
  Printer.print(dba)
  Printer.print(edcba)
}
