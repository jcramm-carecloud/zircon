package org.hexworks.zircon.internal.screen

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.builder.data.TileBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.event.EventBus
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.resource.CP437TilesetResource
import org.hexworks.zircon.api.resource.TilesetResource
import org.hexworks.zircon.internal.component.impl.DefaultLabelTest
import org.hexworks.zircon.internal.event.InternalEvent
import org.hexworks.zircon.internal.grid.RectangleTileGrid
import org.hexworks.zircon.api.animation.AnimationResource
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.util.concurrent.atomic.AtomicBoolean

class TileGridScreenTest {

    lateinit var target: TileGridScreen
    lateinit var tileset: TilesetResource
    lateinit var terminal: TileGrid

    @Before
    fun setUp() {
        tileset = DefaultLabelTest.FONT
        terminal = RectangleTileGrid(
                tileset = tileset,
                size = SIZE)
        MockitoAnnotations.initMocks(this)
        target = TileGridScreen(terminal)
    }

    @Test
    fun givenScreenWithAnimationWhenGivenInputThenFireOnInput() {
        val animation = AnimationResource.loadAnimationFromStream(
                zipStream = this.javaClass.getResourceAsStream("/animations/skull.zap"),
                tileset = tileset)
                .setPositionForAll(Position.create(0, 0))
                .loopCount(0)
                .build()

        val inputFired = AtomicBoolean(false)
        target.onInput { inputFired.set(true) }

        //first of all lets make sure the default behaviour works. if a key is pressed I should get an input fired
        EventBus.broadcast(InternalEvent.Input(KeyStroke('a')))
        assertThat(inputFired.get()).isTrue()

        //now lets add the animation and make sure we can still get input
        target.startAnimation(animation)

        inputFired.set(false)
        EventBus.broadcast(InternalEvent.Input(KeyStroke('a')))
        assertThat(inputFired.get()).isTrue()

    }

    @Test
    fun shouldBeAbleToPutCharacterWhenPutCharacterIsCalled() {
        val char = 'x'
        val expected = TileBuilder.newBuilder()
                .styleSet(target.styleSet())
                .character(char)
                .build()
        val currCursorPos = target.cursorPosition()

        target.putCharacter(char)

        assertThat(target.getTileAt(currCursorPos).get()).isEqualTo(expected)
        assertThat(target.cursorPosition()).isEqualTo(currCursorPos.withRelativeX(1))

    }

    @Test
    fun shouldUseTerminalsFontWhenCreating() {
        assertThat(target.tileset().id)
                .isEqualTo(terminal.tileset().id)
    }

    @Test
    fun shouldProperlyOverrideTerminalFontWhenHasOverrideFontAndDisplayIsCalled() {
        val expectedFont = CP437TilesetResource.AESOMATICA_16X16
        target.useTileset(expectedFont)
        target.display()
        assertThat(target.tileset().id).isEqualTo(expectedFont.id)
        assertThat(terminal.tileset().id).isEqualTo(expectedFont.id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldProperlyThrowExceptionWhenTyringToSetNonCompatibleFont() {
        target.useTileset(CP437TilesetResource.BISASAM_20X20)
    }

    @Test
    fun shouldBeDrawnWhenCharacterSet() {
        target.setTileAt(Position.offset1x1(), CHAR)
        assertThat(target.getTileAt(Position.offset1x1()).get())
                .isEqualTo(CHAR)

    }

    @Test
    fun shouldClearProperlyWhenClearIsCalled() {
        target.setTileAt(Position.offset1x1(), CHAR)
        target.display()

        target.clear()

        assertThat(target.getTileAt(Position.offset1x1()))
                .isNotEqualTo(CHAR)
    }


    companion object {
        val SIZE = Size.create(10, 10)
        val FONT = CP437TilesetResource.ROGUE_YUN_16X16
        val CHAR = TileBuilder.newBuilder()
                .character('x')
                .build()
    }
}
