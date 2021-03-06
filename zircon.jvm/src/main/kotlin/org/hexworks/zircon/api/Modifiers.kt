package org.hexworks.zircon.api

import org.hexworks.zircon.api.modifier.Border
import org.hexworks.zircon.api.builder.modifier.BorderBuilder
import org.hexworks.zircon.api.modifier.SimpleModifiers.*

/**
 * Represents the built-in modifiers supported by zircon.
 */
object Modifiers {

    @JvmStatic
    fun underline() = Underline

    @JvmStatic
    fun blink() = Blink

    @JvmStatic
    fun crossedOut() = CrossedOut

    @JvmStatic
    fun verticalFlip() = VerticalFlip

    @JvmStatic
    fun horizontalFlip() = HorizontalFlip

    @JvmStatic
    fun hidden() = Hidden

    @JvmStatic
    fun glow() = Glow

    /**
     * Shorthand for the default border which is:
     * - a simple border
     * - on all sides (top, right, bottom, left)
     * @see BorderBuilder if you want to create custom borders
     */
    @JvmStatic
    fun border(): Border = BorderBuilder.newBuilder().build()
}
