package com.lightbend.shoppingcart.impl

import akka.{Done, NotUsed}
import com.lightbend.shoppingcart.api.{AddToCartRequest, RemoveFromCartRequest, ShoppingcartService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the ShoppingcartService.
  */
class ShoppingcartServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends ShoppingcartService {

  override def addToCart(id: String): ServiceCall[AddToCartRequest, Done] = ServiceCall { request =>
    val ref = persistentEntityRegistry.refFor[ShoppingcartEntity](id)
    ref.ask(AddToCartCommand(request.product))
  }

  override def showCart(id: String): ServiceCall[NotUsed, List[String]] = ServiceCall { _ =>
    val ref = persistentEntityRegistry.refFor[ShoppingcartEntity](id)
    ref.ask(ShowCartCommand)
  }

  override def removeFromCart(id: String): ServiceCall[RemoveFromCartRequest, Done] = ServiceCall {request =>
    val ref = persistentEntityRegistry.refFor[ShoppingcartEntity](id)
    ref.ask(RemoveFromCartCommand(request.product))
  }
}
