package com.example.drumpads.presentation

sealed class Action {
    data class PlaySample(val atIndex: Int) : Action()
}
