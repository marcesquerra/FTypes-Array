package com.bryghts.ftypes
package async

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag


class Array[T <: async.Any[T]] private(override val future: Future[scala.Array[T]])(override implicit val executionContext: ExecutionContext, flattener: Flattener[T]) extends AnyBase[scala.Array[T], async.Array[T]]{

    def apply(index: async.Int): T =
        flattener(
            for {
                array <- future
                i     <- index.future
            } yield array(i)
        )

    def length: async.Int =
        async.Int(future.map(_.length))

}


object Array
{

    implicit def from[T <: async.Any[T]](implicit b: Flattener[T]) = Builder[scala.Array[T], async.Array[T]]{(f, ec) => new async.Array(f)(ec, b)}

    def apply[T <: async.Any[T]](values: T*)(implicit executionContext: ExecutionContext, flattener: Flattener[T], ct: ClassTag[T]): async.Array[T] =
        from(flattener)(Future.successful(values.toArray))

}

