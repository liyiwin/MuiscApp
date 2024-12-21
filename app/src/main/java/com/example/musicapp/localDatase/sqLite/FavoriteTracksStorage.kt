package com.example.musicapp.localDatase.sqLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.musicapp.bean.local.FavoriteTrack
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.localDatase.sqLite.SQLiteParameterManager.favoriteTracksTableName

class FavoriteTracksStorage(context: Context) :SQLiteDBHelper(context,),IFavoriteTracksStorage{

    private  var dbrw : SQLiteDatabase = this.writableDatabase



    override fun addFavoriteTrack(track: Track) {
        val  values =  ContentValues()
        values.put("id", track.id)
        values.put("name", track.name)
        values.put("url", track.url)
        values.put("albumId", track.album.id)
        values.put("albumName", track.album.name)
        values.put("albumUrl", track.album.url)
        values.put("albumImageUrl", track.album.images[1].url)
        values.put("artistId", track.album.artist.id)
        values.put("artistName", track.album.artist.name)
        values.put("artistUrl", track.album.artist.url)
        values.put("artistImageUrl", track.album.artist.images[1].url)
        dbrw.insert(favoriteTracksTableName, null, values)
    }

    override fun removeFavoriteTrack(trackId:String) {
        dbrw.execSQL("delete from $favoriteTracksTableName where id = '$trackId'");
    }

    override fun getTotalFavoriteTracks(): ArrayList<FavoriteTrack> {
        val result =  ArrayList<FavoriteTrack>()
        val c = dbrw.rawQuery(" SELECT * FROM $favoriteTracksTableName ",null)

        c.moveToFirst()

        for(i in 0 until c.count){
            result.add(
                FavoriteTrack(
                    "${c.getString(0)}",
                    "${c.getString(1)}",
                    "${c.getString(2)}",
                    "${c.getString(3)}",
                    "${c.getString(4)}",
                    "${c.getString(5)}",
                    "${c.getString(6)}",
                    "${c.getString(7)}",
                    "${c.getString(8)}",
                    "${c.getString(9)}",
                    "${c.getString(10)}",
                    )
            )

            c.moveToNext()
        }

        c.close()
        return result;
    }

    override fun checkTrackIsAdded(trackId: String): Boolean {
       val addedTracks = getTotalFavoriteTracks()
        return addedTracks.any { it.id == trackId }
    }

    override  fun createTable(db: SQLiteDatabase?) {
        db?.execSQL(convertCreateSQL())
    }

    override fun dropTable(db: SQLiteDatabase?) {
        db?.execSQL(convertDropText())
    }

    private fun convertCreateSQL():String{
        return "CREATE TABLE $favoriteTracksTableName(" +
                "id text PRIMARY KEY NOT NULL" +
                ", name text NOT NULL" +
                ", url text NOT NULL" +
                ", albumId text NOT NULL" +
                ", albumName text NOT NULL" +
                ", albumUrl text NOT NULL" +
                ", albumImageUrl text NOT NULL" +
                ", artistId text NOT NULL" +
                ", artistName text NOT NULL" +
                ", artistUrl text NOT NULL" +
                ", artistImageUrl text NOT NULL" +
                ")"
    }

    private fun convertDropText():String{
         return "DROP TABLE IF EXISTS $favoriteTracksTableName"
    }

}