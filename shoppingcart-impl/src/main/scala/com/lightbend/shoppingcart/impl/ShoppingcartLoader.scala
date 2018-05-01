package com.lightbend.shoppingcart.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.lightbend.shoppingcart.api.ShoppingcartService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class ShoppingcartLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ShoppingcartApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ShoppingcartApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ShoppingcartService])
}

abstract class ShoppingcartApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[ShoppingcartService](wire[ShoppingcartServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = ShoppingcartSerializerRegistry

  // Register the ShoppingCart persistent entity
  persistentEntityRegistry.register(wire[ShoppingcartEntity])
}
