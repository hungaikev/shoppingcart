package com.lightbend.shoppingcartstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.shoppingcartstream.api.ShoppingcartStreamService
import com.lightbend.shoppingcart.api.ShoppingcartService

import scala.concurrent.Future

/**
  * Implementation of the ShoppingcartStreamService.
  */
class ShoppingcartStreamServiceImpl(shoppingcartService: ShoppingcartService) extends ShoppingcartStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(shoppingcartService.hello(_).invoke()))
  }
}
