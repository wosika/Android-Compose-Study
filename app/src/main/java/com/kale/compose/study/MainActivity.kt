package com.kale.compose.study

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kale.compose.study.ui.theme.ComposestudyTheme

class MainActivity : ComponentActivity() {


    val bottomItems = listOf(
        BottomBarItemData(screen = Screen.Home),
        BottomBarItemData(screen = Screen.Find),
        BottomBarItemData(screen = Screen.Profile)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposestudyTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    MyScreen()
                }
            }
        }
    }

    @Composable
    private fun MyScreen() {
        // 脚手架
        val navControllers = rememberNavController()

        Scaffold(
            bottomBar = { BottomView(navControllers) },
            content = { MyContent(navControllers) })
    }

    @Composable
    private fun MyContent(controller: NavHostController) {
        NavHost(controller, startDestination = Screen.Home.route) {
            // 首页
            composable(Screen.Home.route) { Text(text = Screen.Home.title) }
            // 发现
            composable(Screen.Find.route) { Text(text = Screen.Find.title) }
            // 我的
            composable(Screen.Profile.route) { Text(text = Screen.Profile.title) }
        }

    }

    @Composable
    private fun BottomView(controller: NavHostController) {
        val selectIndex = remember { mutableStateOf(0) }
        BottomNavigation() {
            bottomItems.forEachIndexed { index, bottomBarItemData ->
                BottomNavigationItem(
                    selected = selectIndex.value == index,
                    onClick = {
                        selectIndex.value = index
                        controller.navigate(bottomBarItemData.screen.route)/*{
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                           // popUpTo = navControllers.graph.startDestination
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                        }*/
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = bottomBarItemData.iconRes),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(if (selectIndex.value == index) Color.Blue else Color.Black)
                        )
                    })
            }

        }
    }

    @Composable
    @Preview
    private fun PreviewPage() {
        MyScreen()
    }

}


data class BottomBarItemData(val iconRes: Int = R.drawable.ic_face, val screen: Screen)

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "首页")
    object Find : Screen("find", "发现")
    object Profile : Screen("profile", "我的")

}

