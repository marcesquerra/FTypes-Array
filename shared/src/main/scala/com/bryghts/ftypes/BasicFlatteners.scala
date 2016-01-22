package com.bryghts.ftypes

import com.bryghts.ftypes.async.Flattener

import scala.concurrent.{ExecutionContext, Future}


case class BasicFlattener[T, FT <: async.Any[T, FT]](b:  ExecutionContext => Future[T] => FT) extends Flattener[FT] {
    override def flatten(in: Future[FT])(implicit executionContext: ExecutionContext): FT =
        b(executionContext)(in.flatMap(_.future))
}

/**
 * Created by Marc Esquerrà on 21/01/2016.
 */
trait BasicFlatteners {

    implicit def implicitByteFlattener    : Flattener[async.Byte]       = BasicFlattener(implicit ec => async.Byte.apply)
    implicit def implicitCharFlattener    : Flattener[async.Char]       = BasicFlattener(implicit ec => async.Char.apply)
    implicit def implicitShortFlattener   : Flattener[async.Short]      = BasicFlattener(implicit ec => async.Short.apply)
    implicit def implicitIntFlattener     : Flattener[async.Int]        = BasicFlattener(implicit ec => async.Int.apply)
    implicit def implicitLongFlattener    : Flattener[async.Long]       = BasicFlattener(implicit ec => async.Long.apply)
    implicit def implicitFloatFlattener   : Flattener[async.Float]      = BasicFlattener(implicit ec => async.Float.apply)
    implicit def implicitDoubleFlattener  : Flattener[async.Double]     = BasicFlattener(implicit ec => async.Double.apply)
    implicit def implicitBooleanFlattener : Flattener[async.Boolean]    = BasicFlattener(implicit ec => async.Boolean.apply)
    implicit def implicitUnitFlattener    : Flattener[async.Unit]       = BasicFlattener(implicit ec => async.Unit.apply)

    implicit def implicitArrayFlattener[T <: async.Any[_, _]]: Flattener[async.Array[T]] = BasicFlattener(implicit ec => async.Array.apply[T])

}
