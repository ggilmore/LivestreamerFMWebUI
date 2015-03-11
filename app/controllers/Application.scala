package controllers

import assets.StreamURL
import play.api._
import play.api.data
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

object Application extends Controller {

  val userForm = Form(
    single(
      "url" -> nonEmptyText
    )
  )

  def index = Action {
    Ok(views.html.index(userForm))
  }

  def submit = Action { implicit request =>
    val url = userForm.bindFromRequest.get
    Ok(s"Cool, here is your url:$url")
  }

}