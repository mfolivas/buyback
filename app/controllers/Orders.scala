package controllers

import play.api._
import play.api.mvc._
import models.Order
import play.api.data.Form
import play.api.data.Forms.{ mapping, number, nonEmptyText }
import play.api.i18n.Messages

object Orders extends Controller {

  private val orderForm: Form[Order] = Form(
    mapping(
      "ticker" -> nonEmptyText.verifying(
        "validation.order.duplicate", Order.findByTicker(_).isEmpty),
      "quantity" -> number,
      "start" -> number,
      "end" -> number)(Order.apply)(Order.unapply))

  def list = Action { implicit request =>
    val orders = Order.findAll
    Ok(views.html.orders.list(orders))
  }

  def show(ticker: String) = Action { implicit request =>
    Order.findByTicker(ticker).map { order =>
      Ok(views.html.orders.details(order))
    }.getOrElse(NotFound)
  }

  def newOrder = Action { implicit request =>
    val form = if (flash.get("error").isDefined)
      this.orderForm.bind(flash.data)
    else
      this.orderForm

    Ok(views.html.orders.editOrder(form))
  }

  def save = Action { implicit request =>
    val newOrderForm = this.orderForm.bindFromRequest()

    newOrderForm.fold(
      hasErrors = { form =>
        Redirect(routes.Orders.newOrder).
          flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))
      },
      success = { newOrder =>
        Order.add(newOrder)
        val message = Messages("orders.new.success", newOrder.ticker)
        Redirect(routes.Orders.show(newOrder.ticker)).
          flashing("success" -> message)
      })
  }
}