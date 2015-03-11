package controllers

import models._
import play.api._
import play.api.data
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

object Application extends Controller {



  var urlList:Vector[String] = Vector()

  val userForm = Form(
    single(
      "url" -> nonEmptyText
    )
  )

  def urls = Action{Ok(views.html.urls(Url.getAllUrls))}

  def index = Action {
    Ok(views.html.index(userForm))
  }

  def submit = Action { implicit request =>
    val url = userForm.bindFromRequest.get
    Url.addUrl(url)
    Redirect(routes.Application.urls)
  }

}