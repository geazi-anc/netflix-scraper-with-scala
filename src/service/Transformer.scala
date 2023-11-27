package io.scraper.netflix.service

import io.scraper.netflix.model.Film

import scala.util.Try
import scala.xml.*

object Transformer:
  def removeIllegalTagsOf(xmlContent: String): Try[String] = Try(
    xmlContent
      .replace("<!DOCTYPE html>", "")
      .replace("<br>", ""),
  )

  def loadXmlFrom(xmlString: String): Try[Elem] =
    Try(XML.loadString(xmlString))

  def getFilmsFromTable(table: NodeSeq): Try[Seq[Film]] = Try(
    getDataFrom(table).map {
      case List(rank: String, title: String, weeksInTop10: String) => new Film(rank.toInt, title, weeksInTop10.toInt)
      case _ => throw new IllegalArgumentException("Is not matchable with class List(String, String, String)")
    },
  )

  def getCountries(content: Elem): Try[Set[(String, String)]] = Try(
    (content \\ "a")
      .filter(t => (t \ "@class").text.contains("country-pill") && !t.text.startsWith("#"))
      .map(a => (a.text, (a \ "@href").text))
      .toSet,
  )

  private def getDataFrom(table: NodeSeq): Seq[Seq[String]] =
    table.map(r => (r \ "td").map(c => c.text))
