package io.scraper.netflix.service

import io.scraper.netflix.model.Top10FilmsByCountry

import upickle.default.*

import scala.util.Try

object Loader:
  def saveTop10FilmsByCountryAsJson(
    filePath: String | os.Path,
    top10FilmsByCountry: Seq[Top10FilmsByCountry],
  ): Try[Unit] = filePath match
    case s: String  => Try(os.write.over(os.Path(s), write(top10FilmsByCountry, indent = 2)))
    case p: os.Path => Try(os.write.over(p, write(top10FilmsByCountry, indent = 2)))
