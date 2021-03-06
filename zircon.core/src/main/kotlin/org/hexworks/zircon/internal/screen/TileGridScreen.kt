package org.hexworks.zircon.internal.screen

import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.event.EventBus
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.util.Identifier
import org.hexworks.zircon.internal.behavior.impl.ComponentsLayerable
import org.hexworks.zircon.internal.behavior.impl.DefaultLayerable
import org.hexworks.zircon.internal.component.InternalComponentContainer
import org.hexworks.zircon.internal.component.impl.DefaultContainer
import org.hexworks.zircon.internal.component.impl.DefaultComponentContainer
import org.hexworks.zircon.internal.config.RuntimeConfig
import org.hexworks.zircon.internal.event.InternalEvent
import org.hexworks.zircon.internal.grid.InternalTileGrid
import org.hexworks.zircon.internal.grid.RectangleTileGrid

class TileGridScreen(
        private val tileGrid: TileGrid,
        private val componentsContainer: DefaultContainer = DefaultContainer(
                initialSize = tileGrid.size(),
                position = Position.defaultPosition(),
                componentStyleSet = ComponentStyleSet.defaultStyleSet(),
                initialTileset = tileGrid.tileset()),
        private val buffer: InternalTileGrid = RectangleTileGrid(
                tileset = tileGrid.tileset(),
                size = tileGrid.size(),
                layerable = ComponentsLayerable(
                        layers = DefaultLayerable(tileGrid.size()),
                        components = componentsContainer)),
        private val containerHandler: InternalComponentContainer =
                DefaultComponentContainer(componentsContainer))
    : InternalScreen,
        TileGrid by buffer,
        InternalComponentContainer by containerHandler {

    override val id = Identifier.randomIdentifier()

    init {
        val debug = RuntimeConfig.config.debugMode
        require(tileGrid is InternalTileGrid) {
            "The supplied TileGrid is not an instance of InternalTileGrid."
        }
        EventBus.subscribe<InternalEvent.ScreenSwitch> { (screenId) ->
            if (debug) println("Screen switch event received. screenId: '$screenId'.")
            if (id != screenId) {
                if (debug) println("Deactivating screen")
                deactivate()
            }
        }
        EventBus.subscribe<InternalEvent.RequestCursorAt> { (position) ->
            if (isActive()) {
                tileGrid.setCursorVisibility(true)
                tileGrid.putCursorAt(position)
            }
        }
        EventBus.subscribe<InternalEvent.HideCursor> {
            if (isActive()) {
                tileGrid.setCursorVisibility(false)
            }
        }
    }

    override fun display() {
        EventBus.broadcast(InternalEvent.ScreenSwitch(id))
        setCursorVisibility(false)
        putCursorAt(Position.defaultPosition())
        activate()
        (tileGrid as InternalTileGrid).useContentsOf(buffer)
    }

}
