package exercise

import scala.annotation.tailrec

object Section3 {

  sealed trait List[+A]

  case object Nil extends List[Nothing]

  case class Cons[+A](head: A, tail: List[A]) extends List[A]

  object List {

    def apply[A](as: A*): List[A] = {
      if (as.isEmpty) Nil
      else Cons[A](as.head, apply(as.tail: _*))
    }

    /**
     * EXERCISE 3.7
     * ショート条件を追加するような関数を受け取れば出来そう
     */
    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

    def ∑(ints: List[Int]): Int = foldRight(ints, 0)( _ + _ )
    def ∏(ds: List[Double]): Double = foldRight(ds, 1.0)( _ * _ )

    def sum(ints: List[Int]): Int = ints match {
      case Nil         => 0
      case Cons(x, xs) => x + sum(xs)
    }

    def product(ds: List[Double]): Double = ds match {
      case Nil         => 1.0
      case Cons(x, xs) => x * product(xs)
    }

    /** EXERCISE 3.2 */
    def tail[A](l: List[A]): List[A] = l match {
      case Cons(x, Nil) => Nil
      case Cons(x, xs)  => xs
      case _            => Nil
    }

    /** EXERCISE 3.3 */
    //    def setHead[A](h: A, l: List[A]): List[A] = Cons(h, List.tail(l))
    def setHead[A](h: A, l: List[A]): List[A] = l match {
      case Cons(x, xs)  => Cons(h, xs)
      case Cons(x, Nil) => Cons(h, Nil)
      case _            => Cons(h, Nil)
    }

    /** EXERCISE 3.4 */
    @tailrec
    def drop[A](l: List[A], n: Int): List[A] =
      l match {
        case Cons(x, Nil)          => Nil
        case Cons(x, xs) if n == 1 => xs
        case Cons(x, xs)           => drop(xs, n - 1)
        case _                     => Nil
      }

    /** EXERCISE 3.5 */
    @tailrec
    def dropWhile[A](l: List[A], f: A => Boolean): List[A] =
      l match {
        case Cons(x, Nil) if f(x) => Nil
        case Cons(x, xs) if f(x)  => dropWhile(xs, f)
        case l                    => l
      }

    @tailrec
    def dropWhile2[A](l: List[A])(f: A => Boolean): List[A] =
      l match {
        case Cons(x, Nil) if f(x) => Nil
        case Cons(x, xs) if f(x)  => dropWhile2(xs)(f)
        case l                    => l
      }

    /** EXERCISE 3.6 */
    def init[A](l: List[A]): List[A] =
      l match {
        case Nil                   => Nil
        case Cons(x, Nil)          => Nil
        case Cons(x, Cons(y, Nil)) => Cons(x, Nil)
        case Cons(x, xs)           => Cons(x, init(xs))
      }

    /** EXERCISE 3.9 */
    def length[A](as: List[A]): Int = foldRight(as, 0)( (x: A, y: Int) => 1 + y)

    /** EXERCISE 3.10 */
    @tailrec
    def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B = as match {
      case Nil => z
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
    }
  }

  /** EXERCISE 3.1 */
  val x = List(1, 2, 3, 4, 5) match {
    case Cons(x, Cons(2, Cons(4, _)))          => x
    case Nil                                   => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t)                            => h + List.sum(t)
    case _                                     => 101
  }

  /** EXERCISE 3.8
    * 入力のコピーが生成される。
    * コンストラクタとの関係はコピー？
    */
  val x_3_8 = List.foldRight(List(1,2,3), Nil: List[Int])(Cons(_, _))

}
