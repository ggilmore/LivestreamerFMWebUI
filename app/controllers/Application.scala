package controllers


import Util.GiveURL
import models.StreamHandler
import akka.actor.ActorRef
import akka.util.Timeout
import play.api.libs.json.Json
import scala.concurrent.duration._
import models._
import play.api._
import play.api.data
import akka.pattern.ask
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.libs.Akka

import scala.concurrent.Future
import scala.sys.Prop

object Application extends Controller {

  implicit val timeout = Timeout(2.second)
  implicit val ec = Akka.system.dispatcher

  var urlList:Vector[String] = Vector()

  val userForm = Form(
    single(
      "url" -> nonEmptyText
    )
  )

  def urls = Action{Ok(StreamHandler.toJson)}

  def index = Action {
    Ok(views.html.index(userForm))
  }

  def submit = Action { implicit request =>
    val newUrl = userForm.bindFromRequest.get
    StreamHandler.actorReference ! GiveURL(newUrl)
    Redirect(routes.Application.index)

  }



}