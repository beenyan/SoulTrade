package com.beenyan.soultrade.utils

import com.beenyan.soultrade.SoulTrade
import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class DB {
    companion object {
        lateinit var db: Connection

        fun connect() {
            try {
                val dataPath = SoulTrade.plugin.dataFolder
                if (!dataPath.isDirectory) dataPath.mkdir()
                db = DriverManager.getConnection("jdbc:sqlite:$dataPath/trade.sqlite")
            } catch (e: SQLException) {
                Bukkit.getLogger().severe("Can't connect to database: ${e.message}")
            }

            init()
        }

        private fun init() {
            val sql = """
                CREATE TABLE IF NOT EXISTS trade_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    initiator VARCHAR(16) NOT NULL,
                    receiver VARCHAR(16) NOT NULL,
                    initiator_store TEXT NOT NULL,
                    receiver_store TEXT NOT NULL,
                    time TIMESTAMP DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW'))
                );"""

            try {
                val stmt = db.createStatement()
                stmt.execute(sql)
            } catch (e: SQLException) {
                Bukkit.getLogger().severe("Can't execute sql: ${e.message}")
            }
        }
    }
}