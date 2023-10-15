package com.beenyan.soultrade.trade

import com.beenyan.soultrade.shulkerPreview.Shulker
import com.beenyan.soultrade.trade.item.Close
import com.beenyan.soultrade.trade.item.Confirm
import com.beenyan.soultrade.trade.item.Lock
import com.beenyan.soultrade.utils.I18n
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

enum class TradeStatus {
    Start,
    Pending
}

class Trade(val initiator: Player, val receiver: Player) {
    companion object {
        val tradeList: MutableList<Trade> = mutableListOf()

        fun findByInv(inv: Inventory): Trade? {
            return tradeList.find { trade -> inv == trade.gui.inventory }
        }
    }

    var status = TradeStatus.Pending
    lateinit var close: Close
    lateinit var lock: Lock
    lateinit var confirm: Confirm
    lateinit var gui: GUI
    lateinit var shulker: Shulker

    fun start() {
        this.status = TradeStatus.Start
        this.gui = GUI(this)
        this.close = Close(this)
        this.lock = Lock(this)
        this.confirm = Confirm(this)
        this.shulker = Shulker(this)
        this.cleanExtraTrade()

        Bukkit.getLogger().info(I18n.text("log.startTrade", arrayOf(initiator.name, receiver.name)))
        initiator.openInventory(this.gui.inventory)
        receiver.openInventory(this.gui.inventory)
    }

    private fun cleanExtraTrade() {
        tradeList.filter { it.status == TradeStatus.Pending && (it.receiver == initiator || it.receiver == receiver) }
            .forEach { trade ->
                // 對象開始交易
                trade.initiator.sendMessage(I18n.t("tradeGUI.receiverStartTrade", trade.receiver.name))

                tradeList.remove(trade)
            }
    }
}