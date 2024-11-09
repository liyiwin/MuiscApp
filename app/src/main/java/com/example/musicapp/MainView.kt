package com.example.musicapp

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Back
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.page.HomePage
import com.example.musicapp.page.LoginPage
import com.example.musicapp.page.MusicPlayerPage
import com.example.musicapp.page.OathPage
import com.example.musicapp.page.RecommendPage
import com.example.musicapp.page.SearchPage
import com.example.musicapp.page.SettingPage
import com.example.musicapp.page.SplashPage
import com.example.musicapp.page.TrackListPage
import com.example.musicapp.routing.Screen
import com.example.musicapp.theme.Theme
import com.example.musicapp.viewcomponent.bottomnavigation.BottomNavigationBar
import com.example.musicapp.viewcomponent.bottomnavigation.BottomNavigationItem
import com.example.musicapp.viewmodel.HomeViewModel
import com.example.musicapp.viewmodel.LoginViewModel
import com.example.musicapp.viewmodel.TrackListViewModel
import com.example.musicapp.viewmodel.global.NetWorkViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
fun MainView(netWorkViewModel: NetWorkViewModel){
    Theme {
        AppContent(netWorkViewModel)
    }
 }
@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
fun AppContent(netWorkViewModel: NetWorkViewModel) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val isNavigationPageHidden = remember { mutableStateOf(false) }
    val screenStack = remember{ArrayList<Screen>()}
    val screenState = remember { mutableStateOf<Screen>(Screen.SplashScreen()) }
    val bottomBarSelectedIndex = remember { mutableStateOf<Int>(2) }

    Scaffold (
        scaffoldState = scaffoldState,
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)),
        bottomBar = {
            if(!isNavigationPageHidden.value){
                BottomNavigationComponent(screenStack = screenStack,screenState = screenState,selectedIndex = bottomBarSelectedIndex)
            }else{
                //  隱藏  BottomNavigation
            }
        },
        content = { padding ->
            MainScreenContainer(screenStack,modifier = Modifier.padding(padding), screenState, isNavigationPageHidden,netWorkViewModel)
        }
    )

}

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
fun MainScreenContainer(
    screenStack : ArrayList<Screen>,
    modifier: Modifier = Modifier,
    screenState: MutableState<Screen>,
    isNavigationPageHidden :MutableState<Boolean>,
    netWorkViewModel: NetWorkViewModel,
    ){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {
        when(screenState.value){
            is    Screen.Home -> {
                isNavigationPageHidden.value = false
                val viewModel = viewModel<HomeViewModel>()
                HomePage(viewModel) { screen,pageTitle ->
                    RouterDataStorage.putTrackListTitle(pageTitle);
                    Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
            is  Screen.SearchScreen -> {
                isNavigationPageHidden.value = false
                SearchPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }

            is  Screen.SettingScreen -> {
                isNavigationPageHidden.value = false
                SettingPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }

            is   Screen.RecommendScreen -> {
                isNavigationPageHidden.value = false
                RecommendPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
            is  Screen.MusicPlayerScreen -> {
                isNavigationPageHidden.value = false
                MusicPlayerPage{ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
            is   Screen.TrackListScreen  ->   {
                isNavigationPageHidden.value = true
                val viewModel = viewModel<TrackListViewModel>()
                TrackListPage(
                    RouterDataStorage.getTrackListTitle()
                    ,viewModel,
                    onBackPress = {
                        RouterDataStorage.removeCurrentTrackListTitle()
                        RouterDataStorage.popTrackListTransferData()
                        Back( screenStack = screenStack, screenState = screenState)
                    },
                    navigationController = { screen -> Navigate(
                        screenStack = screenStack,
                        screenState = screenState,
                        page = screen)
                    }
                )
            }
            is  Screen.SplashScreen ->{
                isNavigationPageHidden.value = true
                SplashPage (navigationController = { screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                })
            }
           is  Screen.OauthScreen ->{
                isNavigationPageHidden.value = true
                OathPage(netWorkViewModel,
                 navigationController = { screen -> Navigate(
                        screenStack = screenStack,
                        screenState = screenState,
                        page = screen)
                    })

            }
            is  Screen.LoginScreen ->{
                isNavigationPageHidden.value = true
                val viewModel = viewModel<LoginViewModel>()
                LoginPage(viewModel,
                    navigationController = { screen -> Navigate(
                        screenStack = screenStack,
                        screenState = screenState,
                        page = screen)
                    }
                )
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
            R.string.search_icon, Screen.SearchScreen()),
        BottomNavigationItem(1,
            R.drawable.ic_recommend_selected,
            R.drawable.ic_recommend,
            R.string.recommend_icon, Screen.RecommendScreen()),
        BottomNavigationItem(2,
            R.drawable.ic_home_selected,
            R.drawable.ic_home,
            R.string.home_icon, Screen.Home()),
        BottomNavigationItem(3,
            R.drawable.ic_music_player_selected,
            R.drawable.ic_music_player,
            R.string.music_player_icon, Screen.MusicPlayerScreen()),
        BottomNavigationItem(4,
            R.drawable.ic_personal_selected,
            R.drawable.ic_personal_grey,
            R.string.personal_icon, Screen.SettingScreen()),
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
    if(   page !is Screen.SearchScreen
        &&  page!is Screen.RecommendScreen
        &&  page !is Screen.Home
        &&  page !is Screen.SettingScreen
    ){
        screenStack.add(screenState.value)
    }
    screenState.value = page
}

fun Back(screenStack : ArrayList<Screen>,screenState: MutableState<Screen>){
    screenState.value = screenStack.last()
    screenStack.removeAt(screenStack.count()-1)
}