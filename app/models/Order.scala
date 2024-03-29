package models
case class Order(ticker: String, quantity: Int, start: Int, end: Int)

object Order {
	var orders = Set(
	    Order("GOOG", 50000, 10, 16),
	    Order("AAPL", 100000, 12,16),
	    Order("AMZN", 20000, 11,15),
	    Order("FB", 1000, 13,14))
	
	def findAll = orders.toList.sortBy(_.ticker)
	
	def findByTicker(ticker: String) = orders.find(_.ticker == ticker)
	
	def add(order: Order) {
	  this.orders = this.orders + order
	}
}