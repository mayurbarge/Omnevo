package com.worldofmusic.repository

import cats.effect.IO
import scala.xml.{Elem, XML}

trait SinkRepository[F[_], I, O] {
  def write(filename: String, data: I): F[O]
}

object SinkRepository {
  implicit val fileSinkRepository = new SinkRepository[IO, Elem, Unit] {
    override def write(filename: String, data: Elem): IO[Unit] = IO {
        XML.save(filename, data)
    }
  }
}

