package com.example.musicapp.bean.local

sealed class TrackListTransferData{

    class ChartPlayListTracksTransferData( val playListId:String):TrackListTransferData()

    class FeaturedPlayListTracksTransferData(  val playListId:String):TrackListTransferData()

    class HitPlayListTracksTransferData(  val playListId:String ):TrackListTransferData()

    class ArtistTopTracksTransferData(  val artistd:String ):TrackListTransferData()

}