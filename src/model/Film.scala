package io.scraper.netflix.model

import upickle.default.*

final case class Film(rank: Int, title: String, weeksInTop10: Int)
    derives ReadWriter
