package tech.thatgravyboat.skycubed.config.overlays

import com.teamresourceful.resourcefulconfig.api.annotations.Category
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption.Hidden

@Category("positions")
object OverlayPositions {

    @Hidden @ConfigEntry(id = "rpg")
    val rpg = Position(x = 5, y = 5)

    @Hidden @ConfigEntry(id = "health")
    val health = Position(x = 54, y = 16, scale = 0.5f)

    @Hidden @ConfigEntry(id = "mana")
    val mana = Position(x = 54, y = 10, scale = 0.5f)

    @Hidden @ConfigEntry(id = "defense")
    val defense = Position(x = 90, y = 3)

    @Hidden @ConfigEntry(id = "commissions")
    val commissions = Position(x = 0, y = 100)

}