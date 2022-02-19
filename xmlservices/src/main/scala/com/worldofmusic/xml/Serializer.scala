package com.worldofmusic.xml

import com.worldofmusic.domain.output.{MatchingRelease, Release}

import scala.xml.Elem

trait Serializer[I] {
  def toString(in: I): Elem
}

object Serializer {
  implicit val releaseSerializer = new Serializer[Release] {
    override def toString(in: Release) = {
      <release><name>{in.name}</name><trackCount>{in.trackCount}</trackCount></release>
    }
  }

  implicit val matchingReleaseSerializer = new Serializer[MatchingRelease] {
    override def toString(in: MatchingRelease) = {
      <matchingReleases>{in.releases.map(implicitly[Serializer[Release]].toString(_))}</matchingReleases>
    }
  }
}
