package org.hexworks.zircon.api.input

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.event.EventBus
import org.hexworks.zircon.internal.event.InternalEvent
import org.junit.Test

class InputTypeTest {

    @Test
    fun test() {
        InputType.values().forEach {
            val inputs = mutableListOf<Input>()
            EventBus.subscribe<InternalEvent.Input> { (input) ->
                inputs.add(input)
            }
            EventBus.broadcast(InternalEvent.Input(KeyStroke(
                    character = ' ',
                    type = it)))
            assertThat(inputs.map { it.getInputType() }.first())
                    .isEqualTo(it)
        }
    }


}
