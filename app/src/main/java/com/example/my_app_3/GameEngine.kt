package com.example.my_app_3

import android.view.MotionEvent

class GameEngine(private val screenWidth: Float, private val screenHeight: Float) {

    var cameraX: Float = 0f


    val player = Player(
        x = screenWidth / 2 - 50,
        y = screenHeight - 200,
        width = 100f,
        height = 100f
    )

    private val platforms = mutableListOf<Platform>()

    init {
        // Ground platform
        platforms.add(Platform(0f, screenHeight - 100f, screenWidth * 2, 100f))

        // Floating platforms
        platforms.add(Platform(screenWidth * 0.5f, screenHeight - 250f, 200f, 50f))
        platforms.add(Platform(screenWidth * 1.2f, screenHeight - 400f, 250f, 50f))
        platforms.add(Platform(screenWidth * 1.8f, screenHeight - 250f, 150f, 50f))
        platforms.add(Platform(screenWidth * 2.5f, screenHeight - 500f, 300f, 50f))
        platforms.add(Platform(screenWidth * 3.0f, screenHeight - 350f, 200f, 50f))
    }

    fun update() {
        // Apply gravity
        player.velocityY += GameConstants.GRAVITY

        // Update player position
        player.x += player.velocityX
        player.y += player.velocityY

        // Update camera position to follow the player
        cameraX = player.x - screenWidth / 2

        // Collision detection with platforms
        for (platform in platforms) {
            if (player.x < platform.x + platform.width &&
                player.x + player.width > platform.x &&
                player.y < platform.y + platform.height &&
                player.y + player.height > platform.y) {

                // Collision occurred
                // If player is falling and lands on top of platform
                if (player.velocityY > 0 && player.y + player.height - player.velocityY <= platform.y) {
                    player.y = platform.y - player.height
                    player.velocityY = 0f
                }
                // If player hits bottom of platform
                else if (player.velocityY < 0 && player.y - player.velocityY >= platform.y + platform.height) {
                    player.y = platform.y + platform.height
                    player.velocityY = 0f
                }
                // If player hits side of platform
                else if (player.velocityX > 0 && player.x + player.velocityX <= platform.x) {
                    player.x = platform.x - player.width
                    player.velocityX = 0f
                }
                else if (player.velocityX < 0 && player.x - player.velocityX >= platform.x + platform.width) {
                    player.x = platform.x + platform.width
                    player.velocityX = 0f
                }
            }
        }

        // Keep player within screen bounds (for now, mostly handled by platforms now)
        if (player.y + player.height > screenHeight) {
            player.y = screenHeight - player.height
            player.velocityY = 0f
        }
        if (player.x < 0) {
            player.x = 0f
            player.velocityX = 0f
        }
        if (player.x + player.width > screenWidth) {
            player.x = screenWidth - player.width
            player.velocityX = 0f
        }
    }

    fun jump() {
        // Check if player is on a platform to allow jumping
        var onPlatform = false
        for (platform in platforms) {
            if (player.y + player.height == platform.y &&
                player.x < platform.x + platform.width &&
                player.x + player.width > platform.x) {
                onPlatform = true
                break
            }
        }
        if (onPlatform) {
            player.velocityY = GameConstants.JUMP_STRENGTH
        }
    }

    fun handleTouchEvent(event: MotionEvent?) {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    if (it.x < screenWidth / 3) {
                        player.velocityX = -GameConstants.PLAYER_SPEED
                        player.playerState = PlayerState.RUNNING
                    } else if (it.x > screenWidth * 2 / 3) {
                        player.velocityX = GameConstants.PLAYER_SPEED
                        player.playerState = PlayerState.RUNNING
                    } else {
                        jump()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    player.velocityX = 0f
                    player.playerState = PlayerState.IDLE
                }
            }
        }
    }

    fun getPlatforms(): List<Platform> {
        return platforms
    }
}