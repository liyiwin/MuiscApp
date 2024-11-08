package com.example.musicapp.tool.network

interface Observer<T> {
    fun update(data:T)
}