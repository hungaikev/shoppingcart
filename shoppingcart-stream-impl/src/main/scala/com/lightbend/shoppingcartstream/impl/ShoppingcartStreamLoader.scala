package com.lightbend.shoppingcartstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.lightbend.shoppingcartstream.api.ShoppingcartStreamService
import com.lightbend.shoppingcart.api.ShoppingcartService
import com.softwaremill.macwire._

class ShoppingcartStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ShoppingcartStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ShoppingcartStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ShoppingcartStreamService])
}

abstract class ShoppingcartStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[ShoppingcartStreamService](wire[ShoppingcartStreamServiceImpl])

  // Bind the ShoppingcartService client
  lazy val shoppingcartService = serviceClient.implement[ShoppingcartService]
}
