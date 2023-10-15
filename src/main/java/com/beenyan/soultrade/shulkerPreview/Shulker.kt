package com.beenyan.soultrade.shulkerPreview

import com.beenyan.soultrade.trade.Trade
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class Shulker(private val trade: Trade) {
    companion object {
        val shulkerMap: HashMap<HumanEntity, Trade> = HashMap()

        fun isShulkerBox(item: ItemStack): Boolean {
            return item.type.name.endsWith("SHULKER_BOX")
        }

        fun findByPlayer(player: HumanEntity): Trade? {
            return shulkerMap[player]
        }
    }

    lateinit var gui: GUI

    fun stopPreview(player: HumanEntity) {
        player.openInventory(trade.gui.inventory)
        shulkerMap.remove(player)
    }

    fun preview(shulker: ShulkerBox, event: InventoryClickEvent) {
        this.gui = GUI(shulker)
        shulkerMap[event.whoClicked] = trade
        event.whoClicked.openInventory(this.gui.inventory)
    }
}