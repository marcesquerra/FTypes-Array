package com.bryghts.ftypes
package async

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

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

    def from[T <: async.Any[_, _]](in: Future[scala.Array[T]])(implicit executionContext: ExecutionContext, flattener: Flattener[T]): async.Array[T] =
        new async.Array(in)

    def apply[T <: async.Any[_, _]](values: T*)(implicit executionContext: ExecutionContext, flattener: Flattener[T], ct: ClassTag[T]): async.Array[T] =
        from(Future.successful(values.toArray))
}

