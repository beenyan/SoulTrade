package com.beenyan.soultrade.trade.item

import com.beenyan.soultrade.trade.Trade
import com.beenyan.soultrade.utils.Configs
import com.beenyan.soultrade.utils.I18n
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

enum class LockType {
    UnLock,
    Lock;

    fun isLock(): Boolean {
        return this == Lock
    }
}

class Lock(private val trade: Trade) {
    companion object {
        val lockItem = init(LockType.Lock)
        val unLockItem = init(LockType.UnLock)

        private fun material(lockType: LockType): Material {
            return when (lockType) {
                LockType.UnLock -> Material.ENDER_PEARL
                LockType.Lock -> Material.ENDER_EYE
            }
        }

        private fun name(lockType: LockType): Component {
            return when (lockType) {
                LockType.UnLock -> I18n.t("tradeGUI.item.unLock")
                LockType.Lock -> I18n.t("tradeGUI.item.lock")
            }
        }

        private fun init(lockType: LockType): ItemStack {
            val item = ItemStack(material(lockType))
            val meta = item.itemMeta
            meta.displayName(name(lockType))
            item.itemMeta = meta

            return item
        }
    }

    var initiator = LockType.UnLock
    var receiver = LockType.UnLock

    init {
        render()
    }

    fun click(event: InventoryClickEvent) {
        when (event.slot) {
            Configs.trade.initiatorLockButtonSlot -> {
                if (event.whoClicked != trade.initiator) {
                    return
                }

                initiator = toggle(initiator)
            }

            Configs.trade.receiverLockButtonSlot -> {
                if (event.whoClicked != trade.receiver) {
                    return
                }

                receiver = toggle(receiver)
            }

            else -> return
        }

        render()
        trade.confirm.render()
    }

    fun allLock(): Boolean {
        return initiator == LockType.Lock && receiver == LockType.Lock
    }

    private fun toggle(lockType: LockType): LockType {
        return when (lockType) {
            LockType.UnLock -> LockType.Lock
            LockType.Lock -> {
                initiator = LockType.UnLock
                receiver = LockType.UnLock
                LockType.UnLock
            }
        }
    }

    private fun render() {
        trade.gui.inventory.setItem(Configs.trade.initiatorLockButtonSlot, getItem(initiator))
        trade.gui.inventory.setItem(Configs.trade.receiverLockButtonSlot, getItem(receiver))
    }

    private fun getItem(lockType: LockType): ItemStack {
        return when (lockType) {
            LockType.UnLock -> unLockItem
            LockType.Lock -> lockItem
        }
    }
}