package org.hexworks.zircon.internal.grid

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.event.EventBus
import org.hexworks.zircon.api.input.Input
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.input.MouseActionType
import org.hexworks.zircon.api.input.MouseActionType.*
import org.hexworks.zircon.gui.swing.internal.grid.TerminalMouseListener
import org.hexworks.zircon.internal.event.InternalEvent
import org.junit.Before
import org.junit.Test
import java.awt.Component
import java.awt.event.MouseEvent
import java.util.*

class TileGridMouseListenerTest {

    lateinit var target: TerminalMouseListener
    lateinit var operations: Map<(MouseEvent) -> Unit, MouseActionType>

    val inputs = LinkedList<Input>()

    @Before
    fun setUp() {
        target = TerminalMouseListener(FONT_SIZE, FONT_SIZE)
        operations = mapOf(
                Pair(target::mouseClicked, MOUSE_CLICKED),
                Pair(target::mouseDragged, MOUSE_DRAGGED),
                Pair(target::mouseEntered, MOUSE_ENTERED),
                Pair(target::mouseExited, MOUSE_EXITED),
                Pair(target::mousePressed, MOUSE_PRESSED),
                Pair(target::mouseReleased, MOUSE_RELEASED))
        EventBus.subscribe<InternalEvent.Input> {
            inputs.add(it.input)
        }
    }

    @Test
    fun shouldProperlyHandleMouseEvents() {
        operations.forEach { (op, event) ->
            op.invoke(MOUSE_EVENT)
            assertThat(inputs.poll()).isEqualTo(MouseAction(
                    actionType = event,
                    button = BUTTON,
                    position = POSITION))

        }

    }

    companion object {
        val FONT_SIZE = 16
        val POSITION = Position.create(2, 3)
        val X = POSITION.x * FONT_SIZE
        val Y = POSITION.y * FONT_SIZE
        val BUTTON = 2
        val DUMMY_COMPONENT = object : Component() {}
        val MOUSE_EVENT = MouseEvent(DUMMY_COMPONENT, 1, 1, 1, X, Y, 1, true, BUTTON)
    }
}
