package com.worldofmusic.domain.filters

import com.worldofmusic.domain.input.{Formats, Genre, Label, MusicMozRecord, Name, ReleaseDate, Title, Track}
import org.scalatest.{FunSpec, Matchers}

import java.text.SimpleDateFormat

class FilterRulesSpec extends FunSpec with Matchers {
  describe("Music Moz Filter rule") {

    val tracks = List(Track("Track1"), Track("Track2"), Track("Track3"), Track("Track4"), Track("Track5"), Track("Track6"),
      Track("Track7"), Track("Track8"), Track("Track9"), Track("Track10"), Track("Track11"))
    val releaseDate = new SimpleDateFormat("yyyy.MM.dd").parse("1999.01.01")
    val musicMozMatchingRecord = MusicMozRecord(Title("MusicMoz"), Name("test-name"), Genre("test-genre"), ReleaseDate(releaseDate), Label(""), Formats(""), tracks)
    val musicMozNonMatchingRecord = MusicMozRecord(Title("MusicMoz"), Name("test-name"), Genre("test-genre"), ReleaseDate(releaseDate), Label(""), Formats(""), tracks.tail)

    it("should select the records which have more than 10 tracks and released before 2001-01-01") {
      FilterRules.musicMozFilter(musicMozMatchingRecord) shouldBe true
    }

    it("should not select the records which have less than 10 tracks") {
      FilterRules.musicMozFilter(musicMozNonMatchingRecord) shouldBe false
    }

    it("should not select the records which are released after 2001-01-01") {
      val nonMatchingReleaseDate = new SimpleDateFormat("yyyy.MM.dd").parse("2001.01.02")
      FilterRules.musicMozFilter (
        musicMozMatchingRecord.copy(releasedate = ReleaseDate(nonMatchingReleaseDate))) shouldBe false
    }
  }

}
