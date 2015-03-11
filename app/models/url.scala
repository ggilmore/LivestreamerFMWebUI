package models
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.db.DB


/**
 * Created by gmgilmore on 3/11/15.
 */

case class Url(url: String)

object Url {

  val urlParser = {
      get[String]("url") map {
      case url => Url( url)
    }
  }

  def getAllUrls: Seq[Url] = DB.withConnection { implicit c =>
    SQL("select * from urls").as(urlParser *)
  }

  def addUrl(url: String) = DB.withConnection { implicit c => SQL(s"insert into urls (url) Values('$url')").execute}

}





