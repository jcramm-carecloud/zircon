package org.hexworks.zircon.api.event

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.internal.event.InternalEvent
import org.junit.Test
import java.util.concurrent.atomic.AtomicReference

class EventBusTest {

    val event = AtomicReference<Event>()

    @Test
    fun shouldBeProperlyNotifiedWhenSubscribedEventIsEmitted() {
        EventBus.subscribe<InternalEvent.HideCursor>(this::callback)

        EventBus.broadcast(InternalEvent.HideCursor)

        assertThat(event.get()).isEqualToComparingFieldByField(EVENT)
    }

    private fun callback(event: Event) {
        this.event.set(event)
    }

    data class Obj(val str: String)

    companion object {
        val KEYS = setOf("key")
        val OBJ = Obj("foo")
        val EVENT = InternalEvent.HideCursor
    }
}
