package com.kompani.snek.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.kompani.snek.Main
import ktx.graphics.use

class GameScreen(game: Main) : TemplateScreen(game) {

    override fun show() {
    }

    override fun render(delta: Float) {

        game.update(delta)
        Gdx.gl.glClearColor(255f, 255f, 255f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        renderer.use(ShapeRenderer.ShapeType.Filled) {
            parts.forEach {
                renderer.color = it.color
                renderer.rect(it.x, it.y, 32f, 32f)
            }

            renderer.color = game.food.color
            renderer.rect(game.food.x, game.food.y, game.food.w, game.food.h)

            renderer.projectionMatrix = gameViewport.camera.combined

            renderer.color = game.btn0.color
            renderer.rect(game.btn0.posX, game.btn0.posY, game.btn0.width, game.btn0.height)
            renderer.color = game.btn1.color
            renderer.rect(game.btn1.posX, game.btn1.posY, game.btn1.width, game.btn1.height)
            renderer.color = game.btn2.color
            renderer.rect(game.btn2.posX, game.btn2.posY, game.btn2.width, game.btn2.height)
            renderer.color = game.btn3.color
            renderer.rect(game.btn3.posX, game.btn3.posY, game.btn3.width, game.btn3.height)
        }

        renderer.use(ShapeRenderer.ShapeType.Line) {
            renderer.color = game.snekField.color
            renderer.rect(game.snekField.x, game.snekField.y, game.snekField.w, game.snekField.h)
        }
    }

}