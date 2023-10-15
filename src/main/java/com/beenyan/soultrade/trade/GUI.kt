package com.beenyan.soultrade.trade

import com.beenyan.soultrade.trade.item.Border
import com.beenyan.soultrade.utils.I18n
import kotlin.math.ceil
import kotlin.math.min
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

val borderSlots = arrayListOf(2..6, (13..49 step 9)).flatten().toIntArray()

class GUI(private val trade: Trade) : InventoryHolder {
    val initiatorBag: HashMap<ItemStack, Int> = analyzeInventory(trade.initiator.inventory)
    val receiverBag: HashMap<ItemStack, Int> = analyzeInventory(trade.receiver.inventory)

    companion object {
        val initiatorSlots =
                arrayListOf(9..12, 18..21, 27..30, 36..39, 45..48).flatMap { it.toList() }
        val receiverSlots =
                arrayListOf(14..17, 23..26, 32..35, 41..44, 50..53).flatMap { it.toList() }

        private fun analyzeInventory(inv: Inventory): HashMap<ItemStack, Int> {
            return analyzeInventory(inv.filterNotNull())
        }

        fun analyzeInventory(itemList: List<ItemStack>): HashMap<ItemStack, Int> {
            val bags: HashMap<ItemStack, Int> = HashMap()
            itemList.forEach { item ->
                val matchingItem = bags.keys.find { it.isSimilar(item) }
                if (matchingItem != null) {
                    bags[matchingItem] = bags[matchingItem]!! + item.amount
                } else {
                    bags[item] = item.amount
                }
            }

            return bags
        }
    }

    override fun getInventory(): Inventory = inv

    private val inv = init()

    private fun init(): Inventory {
        val inv = Bukkit.createInventory(this, 54, I18n.t("tradeGUI.title"))
        val border = Border.item
        borderSlots.forEach { slot -> inv.setItem(slot, border) }

        return inv
    }

    fun click(event: InventoryClickEvent) {
        // Player Inventory
        val isInitiator = trade.initiator == event.whoClicked
        val isLock =
                if (isInitiator) trade.lock.initiator.isLock() else trade.lock.receiver.isLock()
        if (isLock) return

        val slots = if (isInitiator) initiatorSlots else receiverSlots
        if (event.clickedInventory?.holder !is GUI) {
            val bags = if (isInitiator) initiatorBag else receiverBag
            addItem(event, bags, slots)
            return
        }

        removeItem(event, slots)
    }

    private fun removeItem(event: InventoryClickEvent, slots: List<Int>) {
        val item = event.currentItem ?: return
        if (!slots.contains(event.slot)) return

        val reduceAmount =
                when (event.action) {
                    InventoryAction.PICKUP_HALF -> ceil(item.amount / 2.0).toInt()
                    InventoryAction.CLONE_STACK,
                    InventoryAction.MOVE_TO_OTHER_INVENTORY,
                    InventoryAction.DROP_ALL_SLOT -> item.amount
                    else -> 1
                }

        item.amount -= reduceAmount
    }

    private fun addItem(
            event: InventoryClickEvent,
            bags: HashMap<ItemStack, Int>,
            slots: List<Int>
    ) {
        val item = event.currentItem ?: return
        val bagItem = bags.entries.find { it.key.isSimilar(item) } ?: return
        val storeAmount = calcAmount(slots, item)
        if (storeAmount >= bagItem.value) return
        val slot =
                slots.find { slot ->
                    val storeItem = inv.getItem(slot)
                    storeItem == null ||
                            storeItem.isSimilar(item) && storeItem.maxStackSize != storeItem.amount
                }
                        ?: return
        val storeItem = inv.getItem(slot)

        val maxMoveAmount =
                min(
                        bagItem.value - storeAmount,
                        if (storeItem == null) item.maxStackSize
                        else storeItem.maxStackSize - storeItem.amount
                )
        val moveAmount =
                when (event.action) {
                    InventoryAction.PICKUP_ALL,
                    InventoryAction.PICKUP_ONE,
                    InventoryAction.NOTHING -> 1
                    InventoryAction.PICKUP_HALF ->
                            min(ceil(item.amount / 2.0).toInt(), maxMoveAmount)
                    else -> min(item.amount, maxMoveAmount)
                }

        if (storeItem == null) {
            val cloneItem = item.clone()
            cloneItem.amount = moveAmount
            inv.setItem(slot, cloneItem)
        } else {
            storeItem.amount += moveAmount
        }
    }

    private fun calcAmount(slots: List<Int>, item: ItemStack): Int {
        return slots.mapNotNull { slot -> inv.getItem(slot) }.filter { it.isSimilar(item) }.fold(
                        0
                ) { sum, it -> sum + it.amount }
    }
}
