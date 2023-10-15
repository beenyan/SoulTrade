package com.beenyan.soultrade.commands.soul_trade

import com.beenyan.soultrade.commands.SubCmd
import com.beenyan.soultrade.trade.Trade
import com.beenyan.soultrade.utils.Configs
import com.beenyan.soultrade.utils.I18n
import com.beenyan.soultrade.utils.distance
import com.beenyan.soultrade.utils.notFoundPlayer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Accept : SubCmd {
    override val name = "accept"

    override fun showHelp(sender: CommandSender): Boolean {
        sender.sendMessage(I18n.t("cmd.st.accept.help"))
        return true
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            showHelp(sender)
            return
        }

        val target = Bukkit.getPlayer(args[0]) ?: return notFoundPlayer(sender as Player, args[0])

        Trade.tradeList.find { it.initiator === target && it.receiver === sender }?.let { trade ->
            // 超過貿易可互動距離
            if (Configs.trade.reach != 0 && Configs.trade.reach < distance(sender as Player, target)
            ) {
                return sender.sendMessage(I18n.t("cmd.st.overTradeReach", target.name))
            }

            return trade.start()
        }

        return sender.sendMessage(I18n.t("cmd.st.accept.notFoundRequest"))
    }
}
