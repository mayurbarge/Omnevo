package com.worldofmusic.xml

import cats.MonadError
import com.worldofmusic.domain.input._
import org.scalatest.{FunSpec, Matchers}

import java.text.SimpleDateFormat

class TransformerSpec extends FunSpec with Matchers {
  type EitherA[A] = Either[String, A]
  implicit val monadErrorInstance = MonadError[EitherA, String]

  describe("Transformer") {
    it("should convert xml to doain object") {
      import Transformer._
      val expectedRecord = MusicMozRecord(Title("TitleABC"), Name("NameABC"), Genre("Folk"),
        ReleaseDate(new SimpleDateFormat("yyyy.MM.dd").parse("1997.08.19")),
        Label("Smithsonian/Folkways"),
        Formats("CD"),
        List(Track("Henry Lee"))
      )

      val xml =
          <root>
            <record>
              <title>TitleABC</title> <name>NameABC</name> <genre>Folk</genre> <releasedate>1997.08.19</releasedate>
              <label>Smithsonian/Folkways</label> <formats>CD</formats>
              <tracklisting>
                <track>Henry Lee</track>
              </tracklisting>
            </record>
          </root>

      xml.toDomain.isRight shouldBe true
      xml.toDomain.getOrElse(Seq.empty) shouldBe Seq(expectedRecord)
    }
  }

}
