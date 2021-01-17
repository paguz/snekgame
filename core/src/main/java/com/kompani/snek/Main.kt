package com.kompani.snek

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.*
import com.kompani.snek.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import org.w3c.dom.css.Rect
import kotlin.random.Random

const val V_WIDTH = 800
const val V_HEIGHT = 600
const val FIELD_START_X = 10f
const val FIELD_START_Y = 128f
const val FIELD_WIDTH = 760f
const val FIELD_HEIGHT = 470f
const val FOOD_SIZE = 16f

data class Button(
        var posX: Float,
        var posY: Float,
        var width: Float,
        var height: Float,
        var color: Color,
        var number: Int
)

data class Rectangle(
        var x: Float,
        var y: Float,
        var w: Float,
        var h: Float,
        var color: Color
)

class Main : KtxGame<KtxScreen>() {
    val batch: Batch by lazy { SpriteBatch() }
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    lateinit var renderer: ShapeRenderer

    var speed = 0.25f
    var parts: MutableList<Rectangle> = mutableListOf(Rectangle(150.0f, 150.0f, 32f, 32f, Color.GREEN))
    var mTimer = 0.0f
    var xVel = 1.0f
    var yVel = 1.0f
    var direction = 0
    var snekDead = false

    var btn0 = Button(364f,60f, 64f, 64f, Color.GREEN, 0)
    var btn1 = Button(364f, 0f, 64f, 64f, Color.GREEN, 1)
    var btn2 = Button(428f, 0f, 64f, 64f, Color.GREEN, 2)
    var btn3 = Button(300f, 0f, 64f, 64f, Color.GREEN, 3)
    var snekField = Rectangle(FIELD_START_X,FIELD_START_Y, FIELD_WIDTH, FIELD_HEIGHT, Color.BLACK)
    var food = Rectangle(Random.nextInt(FIELD_START_X.toInt() + 30, FIELD_WIDTH.toInt() - 30).toFloat(), Random.nextInt(FIELD_START_Y.toInt() + 30, FIELD_HEIGHT.toInt() - 30).toFloat(), FOOD_SIZE, FOOD_SIZE, Color.RED)
    var snekTail = 0
    var feedingCounter = 4
    override fun create() {
        renderer = ShapeRenderer()
        addScreen(GameScreen(this))
        setScreen<GameScreen>()
        for (i in 1..snekTail) {
            parts.add(i, Rectangle(parts.first().x, parts.first().y, 32f, 32f, Color.GREEN))
        }
    }


    fun handleInput() {

        if (Gdx.input.isTouched) {
            var newPoints = Vector2(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat())
            newPoints = gameViewport.unproject(newPoints)

            if (((newPoints.x > btn0.posX && newPoints.x < btn0.posX + btn0.width) && (newPoints.y > btn0.posY && newPoints.y < newPoints.y + btn0.height)) && (direction != 3) ) {
                // UP
                btn0.color = Color.RED
                direction = 1
            } else if ((newPoints.x > btn1.posX && newPoints.x < btn1.posX + btn1.width) && (newPoints.y > btn1.posY && newPoints.y < newPoints.y + btn1.height) && (direction != 1 )) {
                // DOWN
                btn1.color = Color.YELLOW
                direction = 3
            } else if ((newPoints.x > btn2.posX && newPoints.x < btn2.posX + btn2.width) && (newPoints.y > btn2.posY && newPoints.y < newPoints.y + btn2.height) && (direction != 0 )) {
                // RIGHT
                btn2.color = Color.BLACK
                direction = 2
            } else if ((newPoints.x > btn3.posX && newPoints.x < btn3.posX + btn3.width) && (newPoints.y > btn3.posY && newPoints.y < newPoints.y + btn3.height) && (direction != 2 )) {
                // LEFT
                btn3.color = Color.BROWN
                direction = 0
            }
        }
    }

     fun update(delta: Float) {
         if( !snekDead ) {
             mTimer += delta;
             if (mTimer > speed) {
                 mTimer = 0.0f;
                 advance()
             }

             // If not within snekField
             if (parts.first().x > snekField.w - 32) {
                 parts.first().x = snekField.x
             } else if (parts.first().x < snekField.x) {
                 parts.first().x = snekField.w - 32
             } else if (parts.first().y < snekField.y) {
                 parts.first().y = snekField.h + 128
             } else if (parts.first().y > snekField.h + 128) {
                 parts.first().y = snekField.y
             }

             // Eat food
             if (intersect(food, parts.first())) {
                 food = Rectangle(Random.nextInt(FIELD_START_X.toInt() + 30, FIELD_WIDTH.toInt() - 30).toFloat(), Random.nextInt(FIELD_START_Y.toInt() + 30, FIELD_HEIGHT.toInt() - 30).toFloat(), FOOD_SIZE, FOOD_SIZE, Color.RED)
                 speed -= 0.005f
                 feedingCounter--
                 if (feedingCounter == 0) {
                     feedingCounter = 4
                     snekTail++
                     parts.add(snekTail, Rectangle(parts.first().x, parts.first().y, 32f, 32f, Color.GREEN))
                 }
             }

             // Die
             for (i in 1..snekTail) {
                 if (intersect(parts.first(), parts[i])) {
                    snekDead = true
                 }
             }

             handleInput()
         }
    }

    fun advance() {
        if (direction == 0) {
            parts.add(0, Rectangle(parts.first().x - xVel - 32, parts.first().y, 32f,32f, parts.last().color))
        }
        else if (direction == 1) {
            parts.add(0, Rectangle(parts.first().x, parts.first().y + yVel + 32, 32f,32f, parts.last().color))
        }
        else if (direction == 2) {
            parts.add(0, Rectangle(parts.first().x + xVel + 32, parts.first().y, 32f,32f, parts.last().color))
        }
        else if (direction == 3) {
            parts.add(0, Rectangle(parts.first().x, parts.first().y - yVel - 32, 32f,32f, parts.last().color))
        }
        parts.removeLast()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
    }

    fun intersect(r1: Rectangle, r2: Rectangle) : Boolean{
        if ( (r1.y + r1.h < r2.y) || (r1.y > r2.y + r2.h) ) {
            return false
        }
        if ( r1.x + r1.w < r2.x || r1.x > r2.x + r2.w ) {
            return false
        }

        return true
    }

}