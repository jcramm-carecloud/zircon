package org.hexworks.zircon.internal.data

import org.hexworks.zircon.api.data.BaseBlock
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.platform.extension.getOrDefault

data class DefaultBlock(
        override val position: Position3D,
        override val layers: MutableList<Tile>,
        private val sides: Map<BlockSide, Tile>) : BaseBlock() {

    override fun fetchSide(side: BlockSide): Tile {
        return sides.getOrDefault(side, Tile.empty())
    }

}
