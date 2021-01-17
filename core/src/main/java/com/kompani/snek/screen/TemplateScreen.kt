package com.kompani.snek.screen

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.kompani.snek.Main
import com.kompani.snek.Rectangle
import ktx.app.KtxScreen

abstract class TemplateScreen(
        val game: Main,
        val gameViewport: Viewport = game.gameViewport,
        val renderer: ShapeRenderer = game.renderer,
        val parts: List<Rectangle> = game.parts
) : KtxScreen {
    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
    }
}