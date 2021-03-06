package errorhandling

trait Either[+E, +A] {
  /** EXERCISE 4.6 */
  def map[B](f: A => B): Either[E, B] = this match {
    case Right(a) => Right(f(a))
    case Left(e) => Left(e)
  }

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
    case Right(a) => f(a)
    case Left(e) => Left(e)
  }

  def orElse[EE >: E, AA >: A](b: => Either[EE, AA]): Either[EE, AA] = this match {
    case Right(a) => Right(a)
    case Left(_) => b
  }

  def map2[EE >: E, B, C](b: Either[EE, B])( f: (A, B) => C): Either[EE, C] = {
    for {
      aa <- this
      bb <- b
    } yield f(aa, bb)
//    this.map(aa => f(aa, _) ).flatMap(g =>  b.map(g) )
  }



}
case class Left[+E](value: E) extends Either[E, Nothing]
case class Right[+A](value: A) extends Either[Nothing, A]

object Either {
  def mean(xs: IndexedSeq[Double]): Either[String, Double] =
    if(xs.isEmpty) Left("Mean of empty list!")
    else Right(xs.sum / xs.length)

  def safeDiv(x: Int, y: Int): Either[Exception, Int] =
    try Right(x/ y)
    catch { case e: Exception => Left(e) }

  def Try[A](a: => A): Either[Exception, A] =
    try Right(a)
    catch { case e: Exception => Left(e) }

  /** EXERCISE 4.7 */
  def sequence[E, A](as: List[Either[E, A]]): Either[E, List[A]] = {
    as.foldRight[Either[E, List[A]]](Right(Nil))( (ea, acc) => ea.map2(acc)(_ :: _) )
  }

  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = {
    as.foldRight[Either[E, List[B]]](Right(Nil))( (a, acc) => f(a).map2(acc)(_ :: _) )
//    as.foldRight[Either[E, List[B]]](Right(Nil))( (a, acc) => for { b <- f(a); l <- acc} yield b :: l )
  }
}
