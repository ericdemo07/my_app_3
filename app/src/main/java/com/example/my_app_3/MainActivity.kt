package com.example.my_app_3

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.gameView)

        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )

        val leftButton: Button = findViewById(R.id.leftButton)
        val rightButton: Button = findViewById(R.id.rightButton)
        val jumpButton: Button = findViewById(R.id.jumpButton)

        leftButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> gameView.gameEngine.player.velocityX = -GameConstants.PLAYER_SPEED
                MotionEvent.ACTION_UP -> gameView.gameEngine.player.velocityX = 0f
            }
            true
        }

        rightButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> gameView.gameEngine.player.velocityX = GameConstants.PLAYER_SPEED
                MotionEvent.ACTION_UP -> gameView.gameEngine.player.velocityX = 0f
            }
            true
        }

        jumpButton.setOnClickListener { gameView.gameEngine.jump() }
    }
}