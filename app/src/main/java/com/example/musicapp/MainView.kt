package com.example.musicapp

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.page.HomePage
import com.example.musicapp.page.MusicPlayerPage
import com.example.musicapp.page.RecommendPage
import com.example.musicapp.page.SearchPage
import com.example.musicapp.page.SettingPage
import com.example.musicapp.routing.Screen
import com.example.musicapp.theme.Theme
import com.example.musicapp.viewcomponent.bottomnavigation.BottomNavigationBar
import com.example.musicapp.viewcomponent.bottomnavigation.BottomNavigationItem
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainView(){
    Theme {
        AppContent()
    }
 }
@Composable
fun AppContent() {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val isNavigationPageHidden = remember { mutableStateOf(false) }
    val screenStack = remember{ArrayList<Screen>()}
    val screenState = remember { mutableStateOf<Screen>(Screen.Home) }
    val bottomBarSelectedIndex = remember { mutableStateOf<Int>(2) }

    Scaffold (
        scaffoldState = scaffoldState,
        bottomBar = {
            if(!isNavigationPageHidden.value){
                BottomNavigationComponent(screenStack = screenStack,screenState = screenState,selectedIndex = bottomBarSelectedIndex)
            }else{
                //  隱藏  BottomNavigation
            }
        },
        content = { padding ->
            MainScreenContainer(screenStack,modifier = Modifier.padding(padding), screenState, isNavigationPageHidden)
        }
    )

}

@Composable
fun MainScreenContainer(
    screenStack : ArrayList<Screen>,
    modifier: Modifier = Modifier,
    screenState: MutableState<Screen>,
    isNavigationPageHidden :MutableState<Boolean>){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {
        when(screenState.value){
            Screen.Home -> {
                isNavigationPageHidden.value = false
                HomePage { screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
            Screen.SearchScreen -> {
                isNavigationPageHidden.value = false
                SearchPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }

            Screen.SettingScreen -> {
                isNavigationPageHidden.value = false
                SettingPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }

            Screen.RecommendScreen -> {
                isNavigationPageHidden.value = false
                RecommendPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
            Screen.MusicPlayerScreen -> {
                isNavigationPageHidden.value = false
                MusicPlayerPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
        }
    }
}


@Composable
fun BottomNavigationComponent(
    modifier: Modifier = Modifier,
    screenStack : ArrayList<Screen>,
    screenState: MutableState<Screen>,
    selectedIndex : MutableState<Int>
) {
    val items = listOf(
        BottomNavigationItem(0,
            R.drawable.ic_search_selected,
            R.drawable.ic_search_grey,
            R.string.search_icon, Screen.SearchScreen),
        BottomNavigationItem(1,
            R.drawable.ic_recommend_selected,
            R.drawable.ic_recommend,
            R.string.recommend_icon, Screen.RecommendScreen),
        BottomNavigationItem(2,
            R.drawable.ic_home_selected,
            R.drawable.ic_home,
            R.string.home_icon, Screen.Home),
        BottomNavigationItem(3,
            R.drawable.ic_music_player_selected,
            R.drawable.ic_music_player,
            R.string.music_player_icon, Screen.MusicPlayerScreen),
        BottomNavigationItem(4,
            R.drawable.ic_personal_selected,
            R.drawable.ic_personal_grey,
            R.string.personal_icon, Screen.SettingScreen),
    )
    BottomNavigationBar(selectedIndex.value,modifier,items){
        selectedIndex.value = items.indexOf(it)
        Navigate(
            screenStack = screenStack,
            screenState = screenState,
            page = it.screen
        )
    }
}

fun Navigate(screenStack : ArrayList<Screen>,screenState: MutableState<Screen> , page:Screen){
    if(   page != Screen.SearchScreen
        &&  page != Screen.RecommendScreen
        &&  page != Screen.Home
        &&  page != Screen.SettingScreen
    ){
        screenStack.add(screenState.value)
    }
    screenState.value = page
}