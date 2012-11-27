package controllers

import play.api._
import play.api.mvc._
import models.Order

object Orders extends Controller {

  def list = Action { implicit request =>
    val orders = Order.findAll
    Ok(views.html.orders.list(orders))
  }
}