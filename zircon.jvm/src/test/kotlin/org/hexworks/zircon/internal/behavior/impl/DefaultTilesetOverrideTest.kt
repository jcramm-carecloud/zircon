package org.hexworks.zircon.internal.behavior.impl

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.resource.CP437TilesetResource
import org.junit.Before
import org.junit.Test

class DefaultTilesetOverrideTest {

    lateinit var target: DefaultTilesetOverride

    @Before
    fun setUp() {
        target = DefaultTilesetOverride(INITIAL_FONT)
    }

    @Test
    fun shouldReturnInitialFontInitially() {
        assertThat(target.tileset().id)
                .isEqualTo(INITIAL_FONT.id)
    }

    @Test
    fun shouldBeAbleToUseSameSizeFont() {
        val expected = CP437TilesetResource.ROGUE_YUN_16X16

        target.useTileset(expected)

        assertThat(target.tileset().id)
                .isEqualTo(expected.id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowExceptionWhenTryingToSetNonCompatibleFont() {
        target.useTileset(CP437TilesetResource.BISASAM_20X20)
    }

    companion object {
        val INITIAL_FONT = CP437TilesetResource.WANDERLUST_16X16
    }
}
