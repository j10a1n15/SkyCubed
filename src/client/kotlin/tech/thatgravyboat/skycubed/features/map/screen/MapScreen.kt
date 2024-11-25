package tech.thatgravyboat.skycubed.features.map.screen

import com.teamresourceful.resourcefullib.client.screens.BaseCursorScreen
import earth.terrarium.olympus.client.components.Widgets
import earth.terrarium.olympus.client.components.dropdown.DropdownState
import earth.terrarium.olympus.client.layouts.Layouts
import earth.terrarium.olympus.client.ui.UIConstants
import earth.terrarium.olympus.client.utils.State
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.layouts.LayoutElement
import tech.thatgravyboat.skyblockapi.helpers.McPlayer
import tech.thatgravyboat.skyblockapi.utils.text.CommonText
import tech.thatgravyboat.skyblockapi.utils.text.Text
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import tech.thatgravyboat.skycubed.features.map.Maps
import tech.thatgravyboat.skycubed.features.map.pois.Poi
import tech.thatgravyboat.skycubed.utils.ResettingState
import tech.thatgravyboat.skycubed.utils.children

class MapScreen : BaseCursorScreen(CommonText.EMPTY) {

    private val search = State.of("")
    private val xOffset = ResettingState.of {
        McPlayer.self!!.blockPosition().x + Maps.getCurrentOffset().x
    }
    private val zOffset = ResettingState.of {
        McPlayer.self!!.blockPosition().z + Maps.getCurrentOffset().z
    }
    private val scale = State.of(1f)

    private val map = DropdownState.of(Maps.getMapsForLocation())
    private var lastMap = map.get()

    override fun init() {
        if (lastMap != map.get()) {
            xOffset.reset()
            zOffset.reset()
            lastMap = map.get()
        }

        Layouts.column()
            .withChild(Widgets.frame {
                it.withTexture(UIConstants.MODAL_HEADER)
                it.withSize(this.width, 30)
                it.withContentMargin(5)

                it.withLayoutCallback { widget, layout ->
                    val children = layout.children
                    if (children.isEmpty()) return@withLayoutCallback
                    val spacing = (widget.viewWidth - children.sumOf(LayoutElement::getWidth)) / (children.size - 1)
                    var x = widget.x

                    children.forEach { child ->
                        child.x = x
                        x += child.width + spacing
                    }
                }

                it.withContents { contents ->

                    contents.addChild(Widgets.textInput(search) { input ->
                        input.withPlaceholder("Search POIs...")
                        input.withSize(150, 20)
                    })

                    contents.addChild(Widgets.dropdown(
                        map,
                        Maps.getMaps(),
                        { Text.of(it ?: "") },
                        { button -> button.withSize(150, 20) },
                        { }
                    ))
                }
            })
            .withChild(MapsWidget(
                map = map.get(),
                xOffset = xOffset,
                zOffset = zOffset,
                scale = scale,
                filter = { it.enabled && it.filter(search.get()) },
                width = this.width,
                height = this.height - 30
            ))
            .build(this::addRenderableWidget)
    }

    override fun renderBackground(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderTransparentBackground(graphics)
    }

    companion object {

        private fun Poi.filter(search: String): Boolean {
            return search.isEmpty() || this.tooltip.any { it.stripped.contains(search, ignoreCase = true) }
        }
    }
}