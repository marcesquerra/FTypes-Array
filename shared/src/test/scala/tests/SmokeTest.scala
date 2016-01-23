package tests

import com.bryghts.ftypes._
import utest._
import utest.ExecutionContext.RunNow

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable}

object FTypes extends ValExtensions
                 with BasicFlatteners

import FTypes._

/**
 * Created by Marc Esquerrà on 23/01/2016.
 */
object SmokeTest  extends TestSuite{
    val tests = TestSuite{
        "Basic Check" - {
            "Simple Comparisson" - {
                val x = async.Int(1)
                val y = async.Int(2)
                val r = x =!= y
//                assert(await(r))
                r.future.map(r => assert(r))
            }
        }
        "Playing with arrays" - {
            val a = async.Array[async.Int](1, 2, 3)
            val b = a(1)
            val r = b === 2
            r.future.map(r => assert(r))
        }
    }

}
