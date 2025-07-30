package com.example.my_app_3

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {

    private var running: Boolean = false
    private val targetFPS = 60
    private val framePeriod = 1000 / targetFPS

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        var frameCount = 0
        var totalTime: Long = 0
        val oldTime = System.nanoTime()

        while (running) {
            startTime = System.nanoTime()
            canvas = null

            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameView.update()
                    gameView.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = framePeriod - timeMillis

            try {
                if (waitTime > 0) {
                    sleep(waitTime)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            totalTime += System.nanoTime() - startTime
            frameCount++
            if (frameCount == targetFPS) {
                frameCount = 0
                totalTime = 0
            }
        }
    }

    companion object {
        private var canvas: Canvas? = null
    }
}