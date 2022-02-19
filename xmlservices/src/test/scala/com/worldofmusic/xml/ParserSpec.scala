package com.worldofmusic.xml

import cats.MonadError
import com.worldofmusic.domain.input._
import org.scalatest.{FunSpec, Matchers}

import java.text.SimpleDateFormat

class ParserSpec extends FunSpec with Matchers {
  describe("Parser") {
    type EitherA[A] = Either[String, A]
    implicit val monadErrorInstance = MonadError[EitherA, String]

    it("should parse valid date format") {
      val releaseDate = new SimpleDateFormat("yyyy.MM.dd").parse("1997.08.19")
      Parser.releaseDateParser(monadErrorInstance).parse(
      <root><releasedate>1997.08.19</releasedate></root>) shouldBe Right(ReleaseDate(releaseDate))
    }

    it("should throw error when date format is invalid") {
      Parser.releaseDateParser(monadErrorInstance).parse(
        <root><releasedate>1997-08-19</releasedate></root>) shouldBe Left("Invalid date - Unparseable date: \"1997-08-19\"")
    }

    it("should parse Music Moz Record successfully") {
      val expectedRecord = MusicMozRecord(Title("TitleABC"), Name("NameABC"), Genre("Folk"),
        ReleaseDate(new SimpleDateFormat("yyyy.MM.dd").parse("1997.08.19")),
        Label("Smithsonian/Folkways"),
        Formats("CD"),
        List(Track("Henry Lee"))
      )
      val parsedRecord = {
        Parser.recordsParser(monadErrorInstance).parse(
          <root>
            <record>
              <title>TitleABC</title> <name>NameABC</name> <genre>Folk</genre> <releasedate>1997.08.19</releasedate>
              <label>Smithsonian/Folkways</label> <formats>CD</formats>
              <tracklisting>
                <track>Henry Lee</track>
              </tracklisting>
            </record>
          </root>)
      }
      parsedRecord.isRight shouldBe true
      parsedRecord.getOrElse(Seq.empty) shouldBe Seq(expectedRecord)
    }
  }
}
