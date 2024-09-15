package com.example.musicapp.routing

import com.example.musicapp.R

sealed class  Screen(val titleResId:Int){

    object Home:Screen(R.string.home)

    object SearchScreen:Screen(R.string.searchPage)

    object RecommendScreen:Screen(R.string.recommendPage)

    object MusicPlayerScreen:Screen(R.string.musicPlayerPage)

    object SettingScreen:Screen(R.string.settingPage)

    object TrackListScreen:Screen(R.string.trackListPage)


}