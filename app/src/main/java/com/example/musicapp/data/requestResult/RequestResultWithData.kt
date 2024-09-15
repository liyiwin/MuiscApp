package com.example.musicapp.data.requestResult

sealed  class RequestResultWithData<T> {
    class  Success<T>( val message:String,val data:T):RequestResultWithData<T>()
    class  Failure<T>   ( val message:String):RequestResultWithData<T>()
    class  Error<T>(val message:String)  :RequestResultWithData<T>()
}