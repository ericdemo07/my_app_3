package com.example.my_app_3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

import android.util.AttributeSet

class GameView(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private val paint = Paint()
    private var gameThread: GameThread? = null
    lateinit var gameEngine: GameEngine

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val screenWidth = width.toFloat()
        val screenHeight = height.toFloat()

        gameEngine = GameEngine(screenWidth, screenHeight)

        gameThread = GameThread(holder, this)
        gameThread?.setRunning(true)
        gameThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle surface changes if needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameThread?.setRunning(false)
        while (retry) {
            try {
                gameThread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        gameEngine.update()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.BLACK)

        // Draw player
        val player = gameEngine.player
        val playerDrawable = when (player.playerState) {
            PlayerState.IDLE -> context.getDrawable(R.drawable.player_idle)
            PlayerState.RUNNING -> {
                // Simple animation: alternate between two run frames
                if (System.currentTimeMillis() % 500 < 250) {
                    context.getDrawable(R.drawable.player_run1)
                } else {
                    context.getDrawable(R.drawable.player_run2)
                }
            }
            PlayerState.JUMPING -> context.getDrawable(R.drawable.player_jump)
        }
        playerDrawable?.setBounds(
            (player.x - gameEngine.cameraX).toInt(),
            player.y.toInt(),
            (player.x + player.width - gameEngine.cameraX).toInt(),
            (player.y + player.height).toInt()
        )
        playerDrawable?.draw(canvas)

        // Draw platforms
        val platformDrawable = context.getDrawable(R.drawable.platform_sprite)
        for (platform in gameEngine.getPlatforms()) {
            platformDrawable?.setBounds(
                (platform.x - gameEngine.cameraX).toInt(),
                platform.y.toInt(),
                (platform.x + platform.width - gameEngine.cameraX).toInt(),
                (platform.y + platform.height).toInt()
            )
            platformDrawable?.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gameEngine.handleTouchEvent(event)
        return true
    }

    companion object {
        const val TAG = "GameView"
    }
}