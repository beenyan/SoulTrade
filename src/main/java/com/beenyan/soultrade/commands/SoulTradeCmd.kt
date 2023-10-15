package com.beenyan.soultrade.commands

import com.beenyan.soultrade.commands.soul_trade.Accept
import com.beenyan.soultrade.commands.soul_trade.History
import com.beenyan.soultrade.commands.soul_trade.Invite
import com.beenyan.soultrade.commands.soul_trade.Reject
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SoulTradeCmd : CommandExecutor {
    companion object {
        private val subCmd = listOf(Invite(), Accept(), Reject(), History())

        private fun showHelp(sender: CommandSender): Boolean {
            subCmd.forEach { cmd -> cmd.showHelp(sender) }
            return true
        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return true
        if (args.isEmpty()) return showHelp(sender)

        val subCmd = subCmd.find { args[0] == it.name } ?: return showHelp(sender)
        subCmd.execute(sender, args.sliceArray(1..<args.size))
        return true
    }
}