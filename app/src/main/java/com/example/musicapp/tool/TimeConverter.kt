package com.example.musicapp.tool

import kotlin.math.floor

object TimeConverter {
    //timeLong -> second
    fun convertTimeLongToText(timeLong:Long):String{
        val hour = floor((timeLong/3600).toDouble()).toLong()
        val minute =floor(((timeLong-hour*3600)/60).toDouble()).toLong()
        val second = timeLong-minute*60
        val hourText = addZeroIfAmountSmallThanTen(hour)
        val minuteText = addZeroIfAmountSmallThanTen(minute)
        val secondText = addZeroIfAmountSmallThanTen(second)
        if(hour>0) return "$hourText:$minuteText:$secondText"
        else return "$minuteText:$secondText"
    }

    private fun addZeroIfAmountSmallThanTen(amount : Long):String{
        return if(amount < 10) "0$amount"
        else "$amount"
    }


}