package com.worldofmusic.domain.filters

import com.worldofmusic.domain.input.MusicMozRecord
import java.text.SimpleDateFormat

object FilterRules {
     val musicMozFilter: MusicMozRecord => Boolean = {
       val recordSizeGreaterThan10 = (rec: MusicMozRecord) => rec.tracklisting.size > 10
       val dateFormat = new SimpleDateFormat("yyyy.MM.dd")
       val releaseDateBefore1Jan2001 = (rec: MusicMozRecord) => rec.releasedate.date.before(dateFormat.parse("2001.01.01"))
       (record: MusicMozRecord) => recordSizeGreaterThan10(record) && releaseDateBefore1Jan2001(record)
     }
}
