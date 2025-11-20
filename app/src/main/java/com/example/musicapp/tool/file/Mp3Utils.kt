package com.example.musicapp.tool.file

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import com.example.musicapp.bean.local.MP3Metadata
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.mp3.MP3File
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.datatype.Artwork
import java.io.File


object Mp3Utils {


    fun setMp3Metadata(
        mp3File: File,
        title: String,
        artist: String,
        descripe:String,
        albumArtFile: File
    ) {
        try {

            // 讀取 MP3 文件
            val mp3 = AudioFileIO.read(mp3File) as MP3File

            // 取得標籤 (Tag)
            val tag: Tag = mp3.getTagOrCreateAndSetDefault();

            // 設置歌曲名稱和歌手名稱
            tag.setField(FieldKey.TITLE,title)
            tag.setField(FieldKey.ARTIST, artist)

            if (albumArtFile.exists()) {
                val imageBytes = albumArtFile.readBytes()  // 將圖片轉換為字節數組
                val artWork =Artwork()
                artWork.binaryData = imageBytes
                artWork.mimeType = "image/jpeg" // 圖片格式
                artWork.description = descripe // 圖片描述 (例如: 封面)
                tag.addField(artWork)
                Log.d("coverBitmap imageUrl","${artWork.imageUrl}")
                Log.d("File Check", "albumArtFile exists: ${albumArtFile.absolutePath}")
            } else {
                Log.e("File Check", "albumArtFile does not exist!")
            }
            // 設定專輯封面


            // 儲存修改
            mp3.commit()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("coverBitmap imageUrl","${e.message}")
        }
    }

    fun getMp3MetaData(file:File): MP3Metadata {
        val retriever = MediaMetadataRetriever()
        return try {
            // 設置數據源
            val audioFile = AudioFileIO.read(file)
            val tag = audioFile.tag // 標籤
            val audioHeader = audioFile.audioHeader // 音頻頭部信息

            // 提取元數據
            val title = tag?.getFirst(FieldKey.TITLE)
            val artist = tag?.getFirst(FieldKey.ARTIST)
            val duration= audioHeader?.trackLength?:0 // 以秒為單位

            // 提取封面圖片
            val artwork = tag?.firstArtwork
            val coverBitmap = artwork?.binaryData?.let { BitmapFactory.decodeByteArray(it, 0, it.size)  }
            Log.d("coverBitmap artwork","$artwork")
            Log.d("coverBitmap imageUrl","${coverBitmap}")
            // 返回結果
            MP3Metadata(title, artist, duration.toLong(), coverBitmap,file)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("getMp3MetaData",e.message?:"")
            MP3Metadata(null, null, null, null,file)
        } finally {
            // 釋放資源
            retriever.release()
        }
    }

}