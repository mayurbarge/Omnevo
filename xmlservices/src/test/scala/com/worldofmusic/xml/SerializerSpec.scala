package com.worldofmusic.xml

import com.worldofmusic.domain.output.{MatchingRelease, Release}
import org.scalatest.{FunSpec, Matchers}

class SerializerSpec extends FunSpec with Matchers {
  describe("Serializer") {
    it("should serialze releases to xml") {
      val generatedXML = Serializer.releaseSerializer
        .toString(Release("Release1", 122)).text
      val expectedXML = (<release><name>Release1</name><trackCount>122</trackCount></release>).text

      generatedXML shouldBe expectedXML
    }

    it("should serialze Matching Release to xml") {
      val generatedXML = Serializer.matchingReleaseSerializer
        .toString(MatchingRelease(Seq(Release("ABC", 11)))).text
      val expectedXML = (<matchingReleases><release><name>ABC</name><trackCount>11</trackCount></release></matchingReleases>).text

      generatedXML shouldBe expectedXML
    }
  }

}
