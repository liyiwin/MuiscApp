package com.example.musicapp

import androidx.compose.ui.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.page.FavoriteTracksPage
import com.example.musicapp.page.HomePage
import com.example.musicapp.page.localTrackFolderDetail.LocalTrackFolderDetailPage
import com.example.musicapp.page.LoginPage
import com.example.musicapp.page.localTrackFolders.LocalTrackFoldersPage
import com.example.musicapp.page.MusicPlayerPage
import com.example.musicapp.page.OathPage
import com.example.musicapp.page.PersonalInformationPage
import com.example.musicapp.page.RecommendPage
import com.example.musicapp.page.SearchPage
import com.example.musicapp.page.SettingPage
import com.example.musicapp.page.SplashPage
import com.example.musicapp.page.TrackDetailPage
import com.example.musicapp.page.TrackListPage
import com.example.musicapp.routing.Screen
import com.example.musicapp.theme.Theme
import com.example.musicapp.viewcomponent.bottomnavigation.BottomNavigationBar
import com.example.musicapp.viewcomponent.bottomnavigation.BottomNavigationItem
import com.example.musicapp.viewmodel.FavoriteTracksViewModel
import com.example.musicapp.viewmodel.HomeViewModel
import com.example.musicapp.viewmodel.LocalTrackFolderDetailViewModel
import com.example.musicapp.viewmodel.LoginViewModel
import com.example.musicapp.viewmodel.LocalTrackFoldersVIewModel
import com.example.musicapp.viewmodel.MusicPlayerViewModel
import com.example.musicapp.viewmodel.PersonalInformationViewModel
import com.example.musicapp.viewmodel.RecommendViewModel
import com.example.musicapp.viewmodel.SearchViewModel
import com.example.musicapp.viewmodel.TrackDetailViewModel
import com.example.musicapp.viewmodel.TrackListViewModel
import com.example.musicapp.viewmodel.global.NetWorkViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
fun MainView(netWorkViewModel: NetWorkViewModel,externalPageNavigationController:(uri: Uri)->Unit,cleanAppDataController:()->Unit ){
    Theme {
        AppContent(netWorkViewModel,externalPageNavigationController,cleanAppDataController)
    }
 }
@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
fun AppContent(netWorkViewModel: NetWorkViewModel,externalPageNavigationController:(uri: Uri)->Unit,cleanAppDataController:()->Unit ) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val isNavigationPageHidden = remember { mutableStateOf(true) }
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
            MainScreenContainer(screenStack,Modifier, screenState, isNavigationPageHidden,netWorkViewModel,externalPageNavigationController,cleanAppDataController)
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
    externalPageNavigationController:(uri: Uri)->Unit,
    cleanAppDataController:()->Unit
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
                    isNavigationPageHidden.value = false
                    RouterDataStorage.putTrackListTitle(pageTitle);
                    Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
            is  Screen.SearchScreen -> {
                isNavigationPageHidden.value = false
                val viewModel = viewModel<SearchViewModel>()
                SearchPage(viewModel){ screen -> Navigate(
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
                val viewModel = viewModel<RecommendViewModel>()
                RecommendPage(viewModel){ screen -> Navigate(
                    screenStack = screenStack,
                    screenState = screenState,
                    page = screen)
                }
            }
            is  Screen.LocalTrackFoldersScreen -> {
                isNavigationPageHidden.value = false
                val viewModel = viewModel<LocalTrackFoldersVIewModel>()
                LocalTrackFoldersPage( viewModel){ screen -> Navigate(
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
            is   Screen.TrackDetailScreen -> {
                isNavigationPageHidden.value = true
                val viewModel = viewModel<TrackDetailViewModel>()
                TrackDetailPage(viewModel,
                    onBackPress = {
                        RouterDataStorage.popTrack()
                        Back( screenStack = screenStack, screenState = screenState)
                    },
                    navigationController = { screen -> Navigate(
                        screenStack = screenStack,
                        screenState = screenState,
                        page = screen)
                    },
                    externalPageNavigationController = externalPageNavigationController
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

            is Screen.PersonalInformationScreen ->  {
                isNavigationPageHidden.value = true
                val viewModel = viewModel<PersonalInformationViewModel>()
                PersonalInformationPage(
                    viewModel = viewModel,
                    onBackPress = {
                        Back( screenStack = screenStack, screenState = screenState)
                    },
                    navigationController = { screen -> Navigate(
                        screenStack = screenStack,
                        screenState = screenState,
                        page = screen)
                    },
                    cleanAppDataController
                )
            }

            is Screen.FavoriteTracksScreen -> {
                isNavigationPageHidden.value = true
                val viewModel = viewModel<FavoriteTracksViewModel>()
                FavoriteTracksPage(viewModel = viewModel,
                    onBackPress = {
                        Back( screenStack = screenStack, screenState = screenState)
                    },
                    externalPageNavigationController
                 )
            }

            
            is Screen.LocalTrackFolderDetailPage -> {
                isNavigationPageHidden.value = true
                val viewModel = viewModel<LocalTrackFolderDetailViewModel>()
                LocalTrackFolderDetailPage(
                    viewModel,
                    navigationController = {  screen: Screen ->
                        Navigate(
                            screenStack = screenStack,
                            screenState = screenState,
                            page = screen)
                    }
                )

            }
            is Screen.MusicPlayerScreen -> {
                isNavigationPageHidden.value = true
              val viewModel = viewModel<MusicPlayerViewModel>()
               MusicPlayerPage(
                   viewModel,
                   navigationController = { screen: Screen ->
                       Navigate(
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
    val context = LocalContext.current

    val items = listOf(
        BottomNavigationItem(0,
            R.drawable.ic_search_selected,
            R.drawable.ic_search_grey,
            Color(context.getResources().getColor(R.color.searchBottomBarColor,null)),
            R.string.search_icon, Screen.SearchScreen()),
        BottomNavigationItem(1,
            R.drawable.ic_recommend_selected,
            R.drawable.ic_recommend,
            Color(context.getResources().getColor(R.color.recommendBottomBarColor,null)),
            R.string.recommend_icon, Screen.RecommendScreen()),
        BottomNavigationItem(2,
            R.drawable.ic_home_selected,
            R.drawable.ic_home,
            Color(context.getResources().getColor(R.color.homeBottomBarColor,null)),
            R.string.home_icon, Screen.Home()),
        BottomNavigationItem(3,
            R.drawable.ic_music_player_selected,
            R.drawable.ic_music_player,
            Color(context.getResources().getColor(R.color.playerBottomBarColor,null)),
            R.string.music_player_icon, Screen.LocalTrackFoldersScreen()),
        BottomNavigationItem(4,
            R.drawable.ic_personal_selected,
            R.drawable.ic_personal_grey,
            Color(context.getResources().getColor(R.color.settingBottomBarColor,null)),
            R.string.personal_icon, Screen.SettingScreen()),
    )
    BottomNavigationBar(items[selectedIndex.value].background,selectedIndex.value,modifier,items){
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