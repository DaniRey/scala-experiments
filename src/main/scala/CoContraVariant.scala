import scala.util.Random

/**
  * Covariance and contravariance - see Scala Reference 3.5.2 and 4.5
  *
  * code is heavily inspired by http://blog.kamkor.me/Covariance-And-Contravariance-In-Scala/
  *
  * and use a function literal to the constructor for construction in Seller,
  * inspired by http://stackoverflow.com/questions/18442813/structured-type-to-match-class-constructor
  */

//the zoo class hierarchy
abstract class Animal(name: String) {
  def scream(): String

  def who() = name
}

trait Herbivor extends Animal

trait Carnivor extends Animal

trait Omnivor extends Herbivor with Carnivor

class Baer(name: String) extends Animal(name) with Omnivor {
  override def scream(): String = "roar"
}

class Tiger(name: String) extends Animal(name) with Carnivor {
  override def scream(): String = "bruauaaaa"
}

class Cow(name: String) extends Animal(name) with Herbivor {
  override def scream(): String = "muuuu"
}

class InvariantSeller[T <: Animal]

class Seller[+T <: Animal](constructor: String => T) {
  //needs to be defined as "private[this]" List[T]
  private[this] var sold: List[T] = Nil

  def sell(): T = {
    val name = Random.nextString(10)
//    initial idea, provide create method as part of type hierarchy
//    the create method then simply call the constructor
//    val animal = parent.create(name).asInstanceOf[T]
    val animal = constructor(name)
    sold = animal :: sold
    animal
  }
}

class InvariantShelter[T <: Animal]

class Shelter[-T <: Animal] {
  //needs to be defined as "private[this]" List[T]
  //public List[T] is not possible, because then the contravariant type T would appear in a covariant position
  private[this] var animals: List[T] = Nil

  def house(animal: T): Unit = {
    animals = animal :: animals
  }
}

object CoContraVariant extends App {
  val baer = new Baer("Jimmy")
  val tiger = new Tiger("Joe")
  val cow = new Cow("Sibylle")

  var animals: List[Animal] = cow :: Nil
  val carnivors: List[Carnivor] = baer :: tiger :: Nil

  //can connect two Lists of different types, because List is covariant
  //  type List[+A] = scala.collection.immutable.List[A]
  animals = carnivors ::: animals

  def letThemScream(animals: List[Animal]) = animals.foreach(a => println(a.scream()))

  def isJimmy(a: Animal) = a.who() == "Jimmy"

  letThemScream(animals)
  letThemScream(carnivors)

  // this works because Function is Contravariant on the parameter type on Covariant on the return type
  //
  // isJimmy is defined for Animal, carnivors contains Carnivor objects, but because the parameter is contravariant
  // a Function1[Animal, Boolean] may be used with a Carnivor
  //
  // theoretically it might return something more specific than a boolean
  //
  // original:
  // trait Function1[@specialized(scala.Int, scala.Long, scala.Float, scala.Double) -T1, @specialized(scala.Unit, scala.Boolean, scala.Int, scala.Float, scala.Long, scala.Double) +R] extends AnyRef { self =>
  // simplified:
  // trait Function1[-T1,  +R] extends AnyRef { self =>
  // /** Apply the body of this function to the argument.
  //  *  @return   the result of function application.
  // */
  // def apply(v1: T1): R
  val jimmies: List[Carnivor] = carnivors.filter(isJimmy(_))

  //won't compile because InvariantSeller is invariant on it's type parameter
  //var animalSeller: InvariantSeller[Animal] = new InvariantSeller[Carnivor]()

  //compiles because Seller is covariant on its type parameter, this means a more specific type might be provided
  //if you ask for someone who can sell you "animals" it is ok, if you find someone who only sells Carnivors
  var animalSeller: Seller[Animal] = new Seller[Carnivor](new Tiger(_))
  val boughtAnimal: Animal = animalSeller.sell()

  var carnivorSeller: Seller[Carnivor] = new Seller[Omnivor](new Baer(_))
  val boughtCarnivor: Carnivor = carnivorSeller.sell()

  //won't compile because InvariantShelter is invariant on it's type parameter
  //var carnivorShelter: InvariantShelter[Tiger] = new InvariantShelter[Animal]

  //compiles because Shelter is contravariant on its type parameter, this means a more general type might be provided
  //if you look for a shelter for tigers, it is ok to find a shelter which can keep any kind of animals
  var tigerShelter: Shelter[Tiger] = new Shelter[Animal]()
  tigerShelter.house(tiger)

  //doesn't compile because tigerShelter is defined as Shelter[Tiger] and therefore
  //house() as house(animal: Tiger), although the provided object is a Shelter[Animal]
  //tigerShelter.house(baer)

}
