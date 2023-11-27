package io.scraper.netflix.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import upickle.default.*

final case class Top10FilmsByCountry(
  countryName: String,
  url: String,
  weekOf: LocalDate,
  films: Seq[Film],
)

object Top10FilmsByCountry:
  private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  private val serializer: Top10FilmsByCountry => ujson.Obj = x =>
    ujson.Obj(
      "countryName" -> x.countryName,
      "url"         -> x.url,
      "weekOf"      -> x.weekOf.toString,
      "films"       -> writeJs(x.films),
    )

  private val deserializer: ujson.Value => Top10FilmsByCountry = json =>
    new Top10FilmsByCountry(
      countryName = json("countryName").str,
      url = json("url").str,
      weekOf = LocalDate.parse(json("weekOf").str, format),
      films = read[Seq[Film]](json("films")),
    )

  implicit val rw: ReadWriter[Top10FilmsByCountry] =
    readwriter[ujson.Value].bimap[Top10FilmsByCountry](
      serializer,
      deserializer,
    )
