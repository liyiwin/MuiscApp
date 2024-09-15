package com.example.musicapp.viewmodel.state

sealed class RequestState {
    class None(  val message:String):RequestState()

    class Failure(val message:String):RequestState()

    class Loading( val message:String):RequestState()

    class Success(val message:String):RequestState()
}