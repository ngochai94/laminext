package app.tulz.laminext

import app.tulz.laminext.internal.Helpers
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.ownership.Subscription
import com.raquo.laminar.api.L._
import com.raquo.laminar.modifiers.EventPropBinder
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.nodes.ReactiveElement.Base
import org.scalajs.dom

import scala.scalajs.js

class EventPropToSignalPropBinder[Ev <: dom.Event, A](
  propToSignal: EventPropToSignal[Ev, A],
  onNext: A => Unit
) extends EventPropBinder[Ev](propToSignal.key, null, propToSignal.shouldUseCapture) {

  private val eventBus                          = new EventBus[Ev]
  override val domValue: js.Function1[Ev, Unit] = Helpers.eventCallback(eventBus, propToSignal.key, propToSignal.shouldPreventDefault, propToSignal.shouldStopPropagation)

  override def bind(element: Base): DynamicSubscription = {
    ReactiveElement.bindSubscription(element) { c =>
      ReactiveElement.addEventListener(element, this)
      propToSignal.transform(eventBus.events).foreach(onNext)(c.owner)
      new Subscription(c.owner,
                       cleanup = () => {
                         val _ = ReactiveElement.removeEventListener(element, this)
                       }
      )

    }
  }

  override def equals(that: Any): Boolean = {
    that match {
      case setter: EventPropToSignalPropBinder[_, _] if (key == setter.key) && (domValue == setter.domValue) => true
      case _                                                                                                 => false
    }
  }

}