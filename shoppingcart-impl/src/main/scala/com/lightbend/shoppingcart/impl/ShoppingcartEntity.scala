package com.lightbend.shoppingcart.impl

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq


class ShoppingcartEntity extends PersistentEntity {

  override type Command = ShoppingcartCommand[_]
  override type Event = ShoppingcartEvent
  override type State = ShoppingcartState

  /**
    * The initial state. This is used if there is no snapshotted state to be found.
    */
  override def initialState: ShoppingcartState = ShoppingcartState(List.empty)

  /**
    * An entity can define different behaviours for different states, so the behaviour
    * is a function of the current state to a set of actions.
    */
  override def behavior: Behavior = {
    case ShoppingcartState(products) => Actions().onCommand[AddToCartCommand,Done] {
      case (AddToCartCommand(product), context, state) =>
        context.thenPersist(
          AddedToCartEvent(product)
        ) { _ =>
          context.reply(Done)
        }
    }.onReadOnlyCommand[ShowCartCommand.type, List[String]] {
      case (ShowCartCommand, context, state) =>
        context.reply(state.products)
    }.onEvent {
      case (AddedToCartEvent(product), state) =>
        ShoppingcartState(product :: state.products)
    }
  }
}

  /**
    * The current state held by the persistent entity.
    */
  case class ShoppingcartState(products: List[String])

  object ShoppingcartState {
    /**
      * Format for the hello state.
      *
      * Persisted entities get snapshotted every configured number of events. This
      * means the state gets stored to the database, so that when the entity gets
      * loaded, you don't need to replay all the events, just the ones since the
      * snapshot. Hence, a JSON format needs to be declared so that it can be
      * serialized and deserialized when storing to and from the database.
      */
    implicit val format: Format[ShoppingcartState] = Json.format
  }

  /**
    * This interface defines all the events that the ShoppingcartEntity supports.
    */
  sealed trait ShoppingcartEvent extends AggregateEvent[ShoppingcartEvent] {
    def aggregateTag = ShoppingcartEvent.Tag
  }

  object ShoppingcartEvent {
    val Tag = AggregateEventTag[ShoppingcartEvent]
  }

case class AddedToCartEvent(product: String) extends ShoppingcartEvent

object AddedToCartEvent {
  implicit val format: Format[AddedToCartEvent] = Json.format
}

  /**
    * This interface defines all the commands that the HelloWorld entity supports.
    */
  sealed trait ShoppingcartCommand[R] extends ReplyType[R]


  final case class AddToCartCommand(product: String) extends ShoppingcartCommand[Done]

  object AddToCartCommand {

    implicit val format: Format[AddToCartCommand] = Json.format

  }

case object ShowCartCommand extends ShoppingcartCommand[List[String]]

  /**
    * Akka serialization, used by both persistence and remoting, needs to have
    * serializers registered for every type serialized or deserialized. While it's
    * possible to use any serializer you want for Akka messages, out of the box
    * Lagom provides support for JSON, via this registry abstraction.
    *
    * The serializers are registered here, and then provided to Lagom in the
    * application loader.
    */
  object ShoppingcartSerializerRegistry extends JsonSerializerRegistry {
    override def serializers: Seq[JsonSerializer[_]] = Seq(
      JsonSerializer[AddToCartCommand],
      JsonSerializer[AddedToCartEvent],
      JsonSerializer[ShoppingcartState]
    )
  }

