package com.worldofmusic.repository

import cats.effect.IO

import scala.xml.{Elem, XML}

trait SourceRepository[F[_], I, O] {
  def read(filename: I): F[O]
}

object SourceRepository {
  implicit val fileSourceRepository = new SourceRepository[IO, String, Elem] {
    override def read(filename: String): IO[Elem] = IO {
      XML.load(filename)
    }
  }
}