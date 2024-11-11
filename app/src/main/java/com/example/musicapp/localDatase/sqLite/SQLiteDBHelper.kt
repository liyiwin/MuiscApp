package com.example.musicapp.localDatase.sqLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

abstract class SQLiteDBHelper(context: Context) : SQLiteOpenHelper(context,"musicApp.db", null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
       createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        dropTable(db)

        onCreate(db)
    }

    protected abstract fun createTable(db: SQLiteDatabase?);

    protected abstract fun dropTable(db: SQLiteDatabase?);

}