package com.anafthdev.todo.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anafthdev.todo.BuildConfig
import com.anafthdev.todo.R
import com.anafthdev.todo.data.CategoryColor
import com.anafthdev.todo.data.NavigationDestination
import com.anafthdev.todo.di.Application
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.DrawerMenu
import com.anafthdev.todo.ui.*
import com.anafthdev.todo.ui.theme.*
import com.anafthdev.todo.utils.DatabaseUtil
import com.anafthdev.todo.view_model.AppViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainActivity : ComponentActivity() {
	
	@Inject lateinit var databaseUtil: DatabaseUtil
	@Inject lateinit var viewModel: AppViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		(applicationContext as Application).appComponent.inject(this)
		if (BuildConfig.DEBUG) Timber.plant(object : Timber.DebugTree() {
			override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
				super.log(priority, "DEBUG_$tag", message, t)
			}
		})
		
		databaseUtil.categorySize { size ->
			if (size == 0) databaseUtil.insertCategory(
				Category(
					"Any",
					CategoryColor.values[0]
				)
			)
		}
		
		setContent {
			TODOTheme {
				Screen()
			}
		}
	}
	
	@Composable
	fun Screen() {
		val scope = rememberCoroutineScope()
		val scaffoldState = rememberScaffoldState()
		val navigationController = rememberNavController()
		
		val navigationBackStackEntry by navigationController.currentBackStackEntryAsState()
		val currentRoute = navigationBackStackEntry?.destination?.route
		var currentCategoryRoute by remember { mutableStateOf("") }
		
		val todoList by viewModel.todoList.observeAsState(initial = emptyList())
		val categoryList by viewModel.categoryList.observeAsState(initial = emptyList())
		
		viewModel.refresh()
		
		Scaffold(
			scaffoldState = scaffoldState,
			topBar = {
				TopAppBar(
					elevation = 0.dp,
					backgroundColor = primary_container_light,
					title = {
						Text(
							text = "Todo",
							color = black
						)
					},
					navigationIcon = {
						IconButton(
							onClick = {
								scope.launch {
									scaffoldState.drawerState.open()
								}
							}
						) {
							Icon(
								imageVector = Icons.Default.Menu,
								tint = black,
								contentDescription = null
							)
						}
					}
				)
			},
			drawerBackgroundColor = surface_light,
			drawerContent = {
				LazyColumn(
					modifier = Modifier
						.padding(top = 16.dp)
				) {
					item {
						DrawerItem(
							drawerMenu = DrawerMenu(
								name = "Dashboard",
								iconRes = painterResource(id = R.drawable.ic_space_dashboard),
								iconVector = null
							),
							todoCount = todoList.size,
							isSelected = currentRoute == NavigationDestination.DashboardCategory
						) {
							currentCategoryRoute = ""
							scope.launch {
								scaffoldState.drawerState.close()
							}
							
							navigationController.navigate(NavigationDestination.DashboardCategory) {
								navigationController.graph.startDestinationRoute?.let { destination ->
									popUpTo(destination) {
										saveState = true
									}
									
									launchSingleTop = true
									restoreState = true
								}
							}
						}
					}
					
					items(if (categoryList.size >= 3) 3 else categoryList.size) { index ->
						val destination = when (index) {
							0 -> NavigationDestination.Category_1
							1 -> NavigationDestination.Category_2
							2 -> NavigationDestination.Category_3
							else -> throw IndexOutOfBoundsException("Destination to category: $index")
						}
						
						CategoryItem(
							category = categoryList[index],
							isSelected = (currentRoute == destination) or (currentCategoryRoute == destination),
							todoCount = run {
								var todoSize = 0
								databaseUtil.todoSizeByCategoryID(categoryList[index].id) { mTodoSize ->
									todoSize = mTodoSize
								}
								
								todoSize
							}
						) {
							currentCategoryRoute = destination
							scope.launch {
								scaffoldState.drawerState.close()
							}
							
							val route =
								"${NavigationDestination.CategoryScreen}/${categoryList[index].id}"
							navigationController.navigate(route) {
								navigationController.graph.startDestinationRoute?.let { destination ->
									popUpTo(destination) {
										saveState = true
									}
									
									launchSingleTop = true
									restoreState = true
								}
							}
						}
					}
					
					item {
						Column {
							DrawerItem(
								DrawerMenu(
									name = "Complete",
									iconRes = null,
									iconVector = Icons.Default.Check
								),
								todoCount = 0,
								isSelected = currentRoute == NavigationDestination.CompleteScreen
							) {
								currentCategoryRoute = ""
								scope.launch {
									scaffoldState.drawerState.close()
								}
								
								navigationController.navigate(NavigationDestination.CompleteScreen) {
									navigationController.graph.startDestinationRoute?.let { destination ->
										popUpTo(destination) {
											saveState = true
										}
										
										launchSingleTop = true
										restoreState = true
									}
								}
							}
							
							DrawerItem(
								drawerMenu = DrawerMenu(
									name = "Categories",
									iconRes = painterResource(id = R.drawable.ic_category),
									iconVector = null
								),
								todoCount = 0,
								todoCountVisible = false,
								isSelected = currentRoute == NavigationDestination.CategoriesScreen
							) {
								currentCategoryRoute = ""
								scope.launch {
									scaffoldState.drawerState.close()
								}
								
								navigationController.navigate(NavigationDestination.CategoriesScreen) {
									navigationController.graph.startDestinationRoute?.let { destination ->
										popUpTo(destination) {
											saveState = true
										}
										
										launchSingleTop = true
										restoreState = true
									}
								}
							}
						}
					}
					
				}
			},
		) {
			
			NavHost(
				navController = navigationController,
				startDestination = NavigationDestination.DashboardCategory,
			) {
				composable(NavigationDestination.DashboardCategory) {
					DashboardScreen(viewModel)
				}
				
				composable(NavigationDestination.CompleteScreen) {
					CompleteScreen(viewModel)
				}
				
				composable(NavigationDestination.CategoriesScreen) {
					CategoriesScreen(viewModel)
				}
				
				composable(
					route = "${NavigationDestination.CategoryScreen}/{categoryID}",
					arguments = listOf(
						navArgument("categoryID") {
							type = NavType.IntType
						}
					)
				) { entry ->
					val categoryID = entry.arguments?.getInt("categoryID") ?: 0
					CategoryScreen(
						categoryID = categoryID
					)
				}
				
			}
			
		}
	}
	
	@Preview(showSystemUi = true)
	@Composable
	fun ScreenPreview() {
		val scope = rememberCoroutineScope()
		val scaffoldState = rememberScaffoldState(drawerState = DrawerState(DrawerValue.Open))
	
		val categories = listOf(
			Category(
				"Default 1",
				0xFFBA1B1B
			),
			Category(
				"Default 2",
				0xFF827567
			)
		)
		
		Scaffold(
			scaffoldState = scaffoldState,
			topBar = {
				TopAppBar(
					backgroundColor = primary_container_light,
					title = {
						Text(
							text = "Todo"
						)
					},
					navigationIcon = {
						IconButton(
							onClick = {
								scope.launch {
									scaffoldState.drawerState.open()
								}
							}
						) {
							Icon(
								imageVector = Icons.Default.Menu,
								contentDescription = null
							)
						}
					}
				)
			},
			drawerBackgroundColor = surface_light,
			drawerContent = {
				LazyColumn(
					modifier = Modifier
						.padding(top = 16.dp)
				) {
					item {
						DrawerItem(
							drawerMenu = DrawerMenu(
								name = "Dashboard",
								iconRes = painterResource(id = R.drawable.ic_space_dashboard),
								iconVector = null
							),
							todoCount = 100,
							isSelected = true
						) {}
					}
					
					itemsIndexed(categories) { i, category ->
						CategoryItem(
							category = category,
							todoCount = 0,
							isSelected = i ==0
						) {}
					}
					
					item {
						Column {
							DrawerItem(
								DrawerMenu(
									name = "Complete",
									iconRes = null,
									iconVector = Icons.Default.Check
								),
								todoCount = 0,
								isSelected = false
							) {}
							
							DrawerItem(
								drawerMenu = DrawerMenu(
									name = "Categories",
									iconRes = painterResource(id = R.drawable.ic_category),
									iconVector = null
								),
								todoCount = 0,
								todoCountVisible = false,
								isSelected = false
							) {}
						}
					}
					
				}
			},
		) {}
	}
	
}