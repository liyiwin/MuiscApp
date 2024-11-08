package com.example.musicapp.tool.network

interface Subject<T> {
    fun AddObserver(o:Observer<T>)
    fun removeObserver(o:Observer<T>)
    fun notifyObservers(value:T)

}