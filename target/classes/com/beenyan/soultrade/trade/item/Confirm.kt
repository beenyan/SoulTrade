package com.beenyan.soultrade.trade.item

import com.beenyan.soultrade.trade.GUI
import com.beenyan.soultrade.trade.Trade
import com.beenyan.soultrade.utils.Configs
import com.beenyan.soultrade.utils.I18n
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class Confirm(private val trade: Trade) {
    companion object {
        val disableItem = init(false)
        val enableItem = init(true)

        private fun material(enable: Boolean): Material {
            return when (enable) {
                false -> Material.FIREWORK_STAR
                true -> Material.SLIME_BALL
            }
        }

        private fun init(enable: Boolean): ItemStack {
            println("XD")
            val item = ItemStack(material(enable))
            val meta = item.itemMeta
            meta.displayName(I18n.t("tradeGUI.item.confirm"))
            item.itemMeta = meta

            return item
        }
    }

    init {
        render()
    }

    fun render() {
        trade.gui.inventory.setItem(Configs.trade.confirmButtonSlot, getItem(trade.lock.allLock()))
    }

    fun click(event: InventoryClickEvent) {
        if (!trade.lock.allLock()) return
        val initiatorBag =
            GUI.analyzeInventory(GUI.initiatorSlots.mapNotNull { slot -> trade.gui.inventory.getItem(slot) })
        val receiverBag =
            GUI.analyzeInventory(GUI.receiverSlots.mapNotNull { slot -> trade.gui.inventory.getItem(slot) })
        if (isMissingItem(initiatorBag, receiverBag)) {
            trade.gui.inventory.close()
            return
        }
    }

    private fun isMissingItem(initiatorBag: HashMap<ItemStack, Int>, receiverBag: HashMap<ItemStack, Int>): Boolean {
        for ((item, amount) in initiatorBag) {
            trade.gui.initiatorBag.keys.find { it.isSimilar(item) && it.amount >= amount } ?: return true
        }

        for ((item, amount) in receiverBag) {
            trade.gui.receiverBag.keys.find { it.isSimilar(item) && it.amount >= amount } ?: return true
        }

        return false
    }

    private fun getItem(enable: Boolean): ItemStack {
        return if (enable) return enableItem else disableItem
    }
}
