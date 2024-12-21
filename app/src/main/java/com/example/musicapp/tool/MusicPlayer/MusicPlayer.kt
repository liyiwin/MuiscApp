package com.example.musicapp.tool.MusicPlayer

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.musicapp.tool.TimeConverter
import java.io.File
import java.util.Timer
import java.util.TimerTask

class MusicPlayer() {

   private val  mediaPlayer =  MediaPlayer();
   private var  currentMusicPath = ""
   var isPreparing = false
   private var playerProgressTimer : Timer?  = null
   private var progressChangeListener :((progress:Float,currentPosition:String) ->Unit)?= null
   private var musicCompleteCallback:(()->Unit)?= null

   fun init (){
       startObserveProgress()
   }

  fun checkIsNotSetting():Boolean{
      return currentMusicPath.isEmpty()
  }

   fun setMusic(mp3File: File,preparedListener:()->Unit){
       mediaPlayer.stop() // 停止当前播放
       mediaPlayer.reset() // 重置到初始状态
       currentMusicPath = mp3File.absolutePath
       mediaPlayer.setDataSource( mp3File.absolutePath);
       isPreparing = true
       mediaPlayer.prepareAsync();
       mediaPlayer.setOnPreparedListener{
           isPreparing = false
           preparedListener.invoke()
       }
   }

  fun setMusicCompletedCallback(musicCompleteCallback:()->Unit){
      this.musicCompleteCallback = musicCompleteCallback
  }

   fun seekToPosition(progress: Float){
       val duration = mediaPlayer.duration
       mediaPlayer.seekTo((duration*progress).toLong(),MediaPlayer.SEEK_CLOSEST_SYNC)
  }

  fun startMusic(){
      mediaPlayer.start();
  }

   fun pauseMusic(){
       mediaPlayer.pause();
   }


    fun setProgressChangeListener(listener:(progress:Float,currentPosition:String) ->Unit){
        progressChangeListener = listener
    }

    fun updateProgress(progress: Float){
        val duration = mediaPlayer.duration.toFloat()
        val currentPosition = duration*progress
        progressChangeListener?.invoke(progress,TimeConverter.convertTimeLongToText(currentPosition.toLong()/1000))
    }

    private fun startObserveProgress(){
         playerProgressTimer = Timer()
         playerProgressTimer!!.schedule(object: TimerTask() {
            override fun run() {
               val duration = mediaPlayer.duration.toFloat()
                if (duration > 0 && duration < 24 * 60 * 60 * 1000) { // 檢查是否在合理範圍內
                    val currentPosition = mediaPlayer.currentPosition.toFloat()
                    val progress = (currentPosition/duration)
                    if(progress > 0.990)  musicCompleteCallback?.invoke()
                    progressChangeListener?.invoke(progress,TimeConverter.convertTimeLongToText(currentPosition.toLong()/1000))
                }
            }
        },0,1000 )
    }

    private fun stopObserveProgress(){
        if(playerProgressTimer!= null){
            playerProgressTimer!!.cancel()
            playerProgressTimer = null
        }
    }




    fun releaseData(){
        stopObserveProgress()
        mediaPlayer.release()
    }
}