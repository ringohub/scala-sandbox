package errorhandling

sealed trait Option[+A] {
  /** EXERCISE 4.1 */
  def map[B](f: A => B): Option[B] = this match {
    case Some(v) => Some(f(v))
    case None => None
  }

  def flatMap[B](f: A => Option[B]): Option[B] = {
    map(f).getOrElse(None)
  }

  def getOrElse[B >: A](default: => B): B = this match {
    case Some(v) => v
    case None => default
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] = {
    this.map(Some(_)).getOrElse(ob)
  }

  def filter(f: A => Boolean): Option[A] = {
    flatMap( a => if (f(a)) Some(a) else None )
  }
}
case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]

object Option {
  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  /** EXERCISE 4.2 */
  def variance(xs: Seq[Double]): Option[Double] = {
    mean(xs).flatMap( m => mean(xs.map(x => math.pow(x-m, 2))) )
  }

  def lift[A, B](f: A => B): Option[A] => Option[B] = _ map f

  /** EXERCISE 4.3 */
  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a.flatMap( aa => b.map( bb => f(aa, bb) ))
//    for {
//      aa <- a
//      bb <- b
//    } yield { f(aa, bb) }



  /** EXERCISE 4.4 */
  def sequence[A](a: List[Option[A]]): Option[List[A]] =
    a.foldRight[Option[List[A]]](Some(Nil))( (oa, acc) => map2(oa, acc)(_ :: _))

  /** EXERCISE 4.5 */
  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] =
    a.foldRight[Option[List[B]]](Some(Nil))( (x, acc) => map2(f(x), acc)(_ :: _))

  def Try[A](a: => A): Option[A] =
    try Some(a)
    catch {case e: Exception => None}
}
