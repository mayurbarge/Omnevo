package com.worldofmusic.domain.input

final case class MusicMozRecord(
                         title: Title,
                         name: Name,
                         genre: Genre,
                         releasedate: ReleaseDate,
                         label: Label,
                         formats: Formats,
                         tracklisting: List[Track]
                       )
