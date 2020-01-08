package example

import cats.implicits._
import cats.effect.{ContextShift, Effect}
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import scala.util.Random

import org.http4s.circe._
import io.circe.syntax._
import io.circe.generic.extras.auto._
import example.shared.Dto.Foo
import example.shared.Dto.circeConfig

class RestEndpoints[F[_] : Effect]() extends Http4sDsl[F] {
  def endpoints(): HttpRoutes[F] = HttpRoutes.of[F] {
    case request@GET -> Root / "json" / "random" => for {
      randomNumber <- Effect[F].delay(Random.nextInt(100))
      response <- Ok(Foo(randomNumber).asJson)
    } yield response
  }
}

object RestEndpoints {
  def apply[F[_] : ContextShift : Effect](): RestEndpoints[F] = new RestEndpoints[F]()
}
