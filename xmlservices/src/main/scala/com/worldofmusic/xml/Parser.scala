package com.worldofmusic.xml

import cats.implicits.{catsSyntaxApplicativeErrorId, catsSyntaxApplicativeId, toTraverseOps}
import cats.{Applicative, MonadError}
import scala.xml.Node
import scala.util.{Failure, Success, Try}
import com.worldofmusic.domain.input._

trait Parser[F[_], A] {
  def parse(xml: scala.xml.Node): F[A]
}

object Parser {
  implicit def titleParser[F[_]: Applicative] = new Parser[F, Title] {
    override def parse(xml: Node): F[Title] = Title((xml \ "title").text).pure
  }

  implicit def nameParser[F[_]: Applicative] = new Parser[F, Name] {
    override def parse(xml: Node): F[Name] = Name((xml \ "name").text).pure
  }

  implicit def genreParser[F[_]: Applicative] = new Parser[F, Genre] {
    override def parse(xml: Node): F[Genre] = Genre((xml \ "genre").text).pure
  }

  implicit def releaseDateParser[F[_]: MonadError[*[_], String]] = new Parser[F, ReleaseDate] {
    override def parse(xml: Node): F[ReleaseDate] = {
      Try {
        val format = new java.text.SimpleDateFormat("yyyy.MM.dd")
        format.parse((xml \ "releasedate").text)
      } match {
        case Success(value) => Applicative[F].pure(ReleaseDate(value))
        case Failure(exception) => s"Invalid date - ${exception.getMessage}".raiseError
      }
    }
  }

  implicit def labelDateParser[F[_]: Applicative] = new Parser[F, Label] {
    override def parse(xml: Node): F[Label] = Label((xml \ "label").text).pure
  }

  implicit def formatsDateParser[F[_]: Applicative] = new Parser[F, Formats] {
    override def parse(xml: Node): F[Formats] = Formats((xml \ "formats").text).pure
  }

  implicit def tracksParser[F[_]: Applicative] = new Parser[F, List[Track]] {
    override def parse(xml: Node): F[List[Track]] = (xml \ "tracklisting" \ "track").map(track => Track(track.text)).toList.pure
  }

  implicit def recordsParser[F[_]: MonadError[*[_], String]] = new Parser[F, Seq[MusicMozRecord]] {
    override def parse(xml: Node): F[Seq[MusicMozRecord]] = {
      (xml \ "record").map(node => {
        Applicative[F].map7(
          implicitly[Parser[F, Title]].parse(node),
          implicitly[Parser[F, Name]].parse(node),
          implicitly[Parser[F, Genre]].parse(node),
          implicitly[Parser[F, ReleaseDate]].parse(node),
          implicitly[Parser[F, Label]].parse(node),
          implicitly[Parser[F, Formats]].parse(node),
          implicitly[Parser[F, List[Track]]].parse(node),
        )(MusicMozRecord)
      }).sequence
    }
  }
}