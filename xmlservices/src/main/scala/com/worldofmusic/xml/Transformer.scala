package com.worldofmusic.xml

import com.worldofmusic.domain.input.MusicMozRecord
import scala.xml.Elem

object Transformer {
  implicit class XMLToDomain(xml: Elem) {
    def toDomain[F[_]](implicit F: Parser[F, Seq[MusicMozRecord]]) = {
      F.parse(xml)
    }
  }
}