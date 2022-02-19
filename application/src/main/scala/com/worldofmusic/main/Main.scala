package com.worldofmusic.main

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.worldofmusic.domain.filters.FilterRules
import com.worldofmusic.domain.output.{MatchingRelease, Release}
import com.worldofmusic.repository.{SinkRepository, SourceRepository}
import com.worldofmusic.xml.Serializer
import com.worldofmusic.xml.Transformer.XMLToDomain

import scala.util.{Failure, Success, Try}
import scala.xml.Elem

object Main extends App {

  val mainProgram =
  for {
    xml <- SourceRepository.fileSourceRepository.read("in-xml/worldofmusic.xml")
    records <- IO(xml.toDomain)
    program <-IO(
      records.map(
        records => records.filter(FilterRules.musicMozFilter)
      .map(matchedRecord => Release(matchedRecord.name.value, matchedRecord.tracklisting.size)))
      .map(MatchingRelease)
      .map(Serializer.matchingReleaseSerializer.toString))
  } yield {
    program match {
      case Right(value) => saveOutputXML(value)
      case Left(error) => IO(println(error))
    }
  }

  private def saveOutputXML(value: Elem) = {
    Try {
      SinkRepository.fileSinkRepository.write("out-xml/out.xml", value).unsafeRunSync()
    } match {
      case Success(_) => IO(println(s"Output file saved successfully."))
      case Failure(error) => IO(println(s"Error while writing output file. ${error.getMessage}"))
    }
  }

  mainProgram.flatten.unsafeRunSync()

}
