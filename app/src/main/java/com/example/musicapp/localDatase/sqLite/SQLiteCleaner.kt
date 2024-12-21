package com.example.musicapp.localDatase.sqLite

import android.content.Context
import com.example.musicapp.localDatase.sqLite.SQLiteParameterManager.appDBName
import com.example.musicapp.localDatase.sqLite.SQLiteParameterManager.favoriteTracksTableName

object SQLiteCleaner {

    fun clearFavoriteTracksStorage(context: Context) {
        val db = context.openOrCreateDatabase(appDBName, Context.MODE_PRIVATE, null)
        db.execSQL("DELETE FROM $favoriteTracksTableName")  // 清除資料表的所有資料
        db.close()
    }
}