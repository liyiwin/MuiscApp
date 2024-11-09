package com.example.musicapp.routing

import com.example.musicapp.R

sealed class  Screen(val titleResId:Int){

    class Home:Screen(R.string.home)

    class SearchScreen:Screen(R.string.searchPage)

    class RecommendScreen:Screen(R.string.recommendPage)

    class MusicPlayerScreen:Screen(R.string.musicPlayerPage)

    class SettingScreen:Screen(R.string.settingPage)

    class TrackListScreen:Screen(R.string.trackListPage)


}