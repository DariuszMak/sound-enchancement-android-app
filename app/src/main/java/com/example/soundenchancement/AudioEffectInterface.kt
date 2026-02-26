package com.example.soundenchancement

interface IBassBoost {
    var strength: Short
    var enabled: Boolean
    fun release()
}