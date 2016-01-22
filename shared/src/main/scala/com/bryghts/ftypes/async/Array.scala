package com.bryghts.ftypes
package async

import scala.concurrent.{ExecutionContext, Future}

trait Flattener[T <: async.Any[_, _]] {
    def flatten(in: Future[T])(implicit executionContext: ExecutionContext): T
}

class Array[T <: async.Any[_, _]] private(override val future: Future[scala.Array[T]])(override implicit val executionContext: ExecutionContext, flattener: Flattener[T]) extends Any[scala.Array[T], async.Array[T]]{

    def apply(index: async.Int): T =
        flattener.flatten(
            for {
                array <- future
                i     <- index.future
            } yield array(i)
        )

    def length: async.Int =
        async.Int(future.map(_.length))

}


object Array // extends AnyCompanion[scala.Array[_], async.Array[_]]
{

    def apply[T](in: Future[scala.Array[T]])(implicit executionContext: ExecutionContext): Array[T] =
        new async.Array(in)

}

