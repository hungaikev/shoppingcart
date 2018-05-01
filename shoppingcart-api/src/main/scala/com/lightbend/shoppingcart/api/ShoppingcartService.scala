package com.lightbend.shoppingcart.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}


/**
  * The ShoppingCart service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the ShoppingcartService.
  */
trait ShoppingcartService extends Service {

  def addToCart(id: String): ServiceCall[AddToCartRequest,Done]

  def showCart(id: String): ServiceCall[NotUsed, List[String]]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("shoppingcart")
      .withCalls(
        restCall(Method.POST,"/api/add-to-cart/:id", addToCart _),
        restCall(Method.GET,"/api/cart/:id", showCart _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}

case class AddToCartRequest(product: String)

object AddToCartRequest {
  implicit val format: Format[AddToCartRequest] = Json.format[AddToCartRequest]
}
