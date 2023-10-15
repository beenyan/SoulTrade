package com.beenyan.soultrade.utils

import com.beenyan.soultrade.SoulTrade
import com.google.common.base.Charsets
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStreamReader
import java.util.*

enum class LanguageType(private val value: String) {
    ZH_TW("ZH-TW");

    override fun toString(): String {
        return value
    }
}

class I18n {
    companion object {
        private val i18nMap: EnumMap<LanguageType, FileConfiguration> = EnumMap(LanguageType::class.java)
        private val langType: LanguageType = LanguageType.ZH_TW

        fun t(key: String, vararg args: String, italic: Boolean = false): Component {
            return LegacyComponentSerializer.legacySection().deserialize(text(key, args))
                .decoration(TextDecoration.ITALIC, italic)
        }

        fun text(key: String, args: Array<out String> = emptyArray()): String {
            val notFoundText = "{$langType.$key}"
            val i18nFile = i18nMap[langType] ?: return notFoundText
            var i18nSerializeText = i18nFile.getString(key) ?: return notFoundText
            args.forEachIndexed { index, s -> i18nSerializeText = i18nSerializeText.replace("{$index}", s) }
            return i18nSerializeText
        }

        fun load() {
            val fileName = "language/${LanguageType.ZH_TW}.yml"
            val defConfigStream = SoulTrade.plugin.getResource(fileName)
                ?: return Bukkit.getLogger().severe("Language File Not Found")

            val zhTW = YamlConfiguration.loadConfiguration(
                InputStreamReader(
                    defConfigStream,
                    Charsets.UTF_8
                )
            ) as FileConfiguration

            i18nMap[LanguageType.ZH_TW] = zhTW
        }
    }

}
