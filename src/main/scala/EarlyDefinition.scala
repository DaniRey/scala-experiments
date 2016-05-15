/**
  * Early definition may be used to provide values to the constructor of a super class
  * see Scala Reference 5.1.5 Early Definitions for details
  */
object EarlyDefinition extends App{
  class Animal() {
    val amountOfLegs: Int = 4

    println(amountOfLegs)
  }

  class NoOverride() extends Animal {}

  class Override() extends Animal {
    override val amountOfLegs: Int = 2
  }

  class Early() extends {override val amountOfLegs: Int = 2} with Animal

  val animal = new Animal()
  val withOverride = new Override()
  val noOverride = new NoOverride()
  val early = new Early()
}
