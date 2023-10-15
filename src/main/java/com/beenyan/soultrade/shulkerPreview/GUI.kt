package com.beenyan.soultrade.shulkerPreview

import com.beenyan.soultrade.shulkerPreview.item.Border
import com.beenyan.soultrade.utils.I18n
import org.bukkit.Bukkit
import org.bukkit.block.ShulkerBox
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

val borderSlots = arrayListOf((9..17), (45..53)).flatten().toIntArray()
private const val SHULKER_BOX_OFFSET = 18

class GUI(private val shulker: ShulkerBox) : InventoryHolder {
    override fun getInventory(): Inventory = inv

    private val inv = init()

    private fun init(): Inventory {
        val inv = Bukkit.createInventory(this, 54, I18n.t("ShulkerGUI.title"))
        val border = Border.getByColor(shulker.color)
        
        borderSlots.forEach { slot -> inv.setItem(slot, border) }

        (0..<InventoryType.SHULKER_BOX.defaultSize).forEach { i -> inv.setItem(i + SHULKER_BOX_OFFSET, shulker.inventory.getItem(i)) }

        return inv
    }
}
