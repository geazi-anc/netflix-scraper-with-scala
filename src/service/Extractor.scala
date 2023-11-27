package io.scraper.netflix.service

import sttp.client4.quick.*
import sttp.model.Uri

import scala.util.{Failure, Success, Try}

object Extractor:
  def getMostPopularFilmsFrom(url: Uri): Try[String] =
    val response   = quickRequest.get(url).send()
    val statusCode = response.code.toString.toInt

    if statusCode == 200 then Success(response.body)
    else Failure(new Exception(s"Failed to access $url, status $statusCode"))

  def getCountries: Try[String] =
    val url        = uri"https://www.netflix.com/tudum/top10/united-states"
    val response   = quickRequest.get(url).send()
    val statusCode = response.code.toString.toInt

    if statusCode == 200 then Success(response.body)
    else Failure(new Exception(s"Failed to get countries from $url, status $statusCode"))
