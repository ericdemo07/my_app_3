package com.example.my_app_3

data class Player(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var velocityX: Float = 0f,
    var velocityY: Float = 0f,
    var playerState: PlayerState = PlayerState.IDLE
)