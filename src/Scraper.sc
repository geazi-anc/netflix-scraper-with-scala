//> using toolkit "latest"
//> using dep org.scala-lang.modules::scala-xml:2.2.0
//> using dep com.outr::scribe:3.12.2

import java.time.LocalDate

import io.scraper.netflix.model.*
import io.scraper.netflix.service.*

import scribe.format.*
import sttp.model.Uri
import sttp.model.Uri.UriContext
import upickle.default.*

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
import scala.xml.*

// default variables
val baseUrl      = "https://www.netflix.com"
val jsonFilePath = os.pwd / "top10_films_of_netflix_by_country.json"
val customFormat = formatter"$date $level   $messages$newLine"
val weekOf       = LocalDate.now()

// main
scribe.Logger.root
  .clearHandlers()
  .clearModifiers()
  .withHandler(formatter = customFormat)
  .replace()

scribe.info("Netflix Scraper started")
scribe.info("Extracting countries available at Netflix")

val extractedCountries = for
  response  <- Extractor.getCountries
  xmlString <- Transformer.removeIllegalTagsOf(response)
  content   <- Transformer.loadXmlFrom(xmlString)
  countries <- Transformer.getCountries(content)
yield countries

extractedCountries match
  case Success(r) => scribe.info(s"${r.size} countries were successfully extracted")
  case Failure(e) =>
    scribe.error("Failed to extract countries available at Netflix")
    throw e

val countries =
  extractedCountries.get.toList.map((countryName, url) => (countryName, baseUrl + url))

scribe.info("Starting to extract most popular films for each countries")

val extractAndTransformFilmsFrom: Uri => Future[Try[Seq[Film]]] = url =>
  Future {
    for
      response  <- Extractor.getMostPopularFilmsFrom(url)
      xmlString <- Transformer.removeIllegalTagsOf(response)
      content   <- Transformer.loadXmlFrom(xmlString)
      filmTable <- Try(content \\ "tbody" \ "tr")
      films     <- Transformer.getFilmsFromTable(filmTable)
    yield films
  }

val top10FilmsByCountries = countries
  .map((countryName, url) => (countryName, url, extractAndTransformFilmsFrom(uri"$url")))
  .map { (countryName, url, future) =>
    val films               = Await.result(future, 3.seconds)
    val top10FilmsByCountry =
      for films <- films
      yield Top10FilmsByCountry(countryName, url, weekOf, films)

    (countryName, url, top10FilmsByCountry)
  }

val results = top10FilmsByCountries.map { (countryName, url, top10FilmsByCountry) =>
  top10FilmsByCountry match
    case Success(r) =>
      scribe.info(s"Succeed to extract top10 films of $countryName: $url")
      Success(r)
    case Failure(e) =>
      scribe.error(s"Failed to extract top10 films of $countryName. Reason: $e")
      Failure(e)
}.groupBy {
  case Success(r) => "success"
  case Failure(e) => "failure"
}

val (succeed, failed) = (results("success").length, results("failure").length)

scribe.info(s"Extracted top10 films from $succeed countries with success and $failed with failure")
scribe.info(s"Saving json data at $jsonFilePath")

if succeed == 0 then throw new NoSuchElementException("Zero extraction was succeed. Nothing to save")
val top10FilmsByCountryList = results("success").map(_.get).sortBy(_.countryName)

Loader.saveTop10FilmsByCountryAsJson(jsonFilePath, top10FilmsByCountryList) match
  case Success(r) => scribe.info(s"Saved at $jsonFilePath")
  case Failure(e) => throw e

scribe.info("Netflix Scraper successfully finished")
