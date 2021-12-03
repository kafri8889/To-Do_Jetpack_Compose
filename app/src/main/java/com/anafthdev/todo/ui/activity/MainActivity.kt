package com.anafthdev.todo.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.anafthdev.todo.common.AppViewModel
import com.anafthdev.todo.data.CategoryColor
import com.anafthdev.todo.data.NavigationDestination
import com.anafthdev.todo.di.Application
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.DrawerMenu
import com.anafthdev.todo.ui.*
import com.anafthdev.todo.ui.theme.*
import com.anafthdev.todo.utils.AppUtil.toast
import com.anafthdev.todo.utils.DatabaseUtil
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainActivity : ComponentActivity() {
	
	@Inject lateinit var appRepository: DatabaseUtil
	@Inject lateinit var viewModel: AppViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		(applicationContext as Application).appComponent.inject(this)
		if (BuildConfig.DEBUG) Timber.plant(object : Timber.DebugTree() {
			override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
				super.log(priority, "DEBUG_$tag", message, t)
			}
		})
		
		appRepository.categorySize { size ->
			if (size == 0) appRepository.insertCategory(
				Category(
					id = 1,
					name = "Any",
					color = CategoryColor.values[0]
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
							text = when (currentRoute) {
								NavigationDestination.DashboardScreen -> NavigationDestination.DashboardScreen
								NavigationDestination.CompleteScreen -> NavigationDestination.CompleteScreen
								"${NavigationDestination.CategoryScreen}/{categoryID}" -> NavigationDestination.CategoryScreen
								"${NavigationDestination.CategoriesScreen}/{categoryID}" -> NavigationDestination.CategoriesScreen
								"${NavigationDestination.EditTodoScreen}/{todoID}" -> NavigationDestination.EditTodoScreen
								else -> NavigationDestination.DashboardScreen
							},
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
			drawerShape = RoundedCornerShape(0),
			drawerContent = {
				LazyColumn(
					modifier = Modifier.padding(top = 16.dp)
				) {
					item {
						DrawerItem(
							drawerMenu = DrawerMenu(
								name = "Dashboard",
								iconRes = painterResource(id = R.drawable.ic_space_dashboard),
								iconVector = null
							),
							todoCount = todoList.size,
							isSelected = currentRoute == NavigationDestination.DashboardScreen
						) {
							currentCategoryRoute = ""
							scope.launch {
								scaffoldState.drawerState.close()
							}
							
							navigationController.navigate(NavigationDestination.DashboardScreen) {
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
						val c = LocalContext.current
						val destination = when (index) {
							0 -> NavigationDestination.Category_1
							1 -> NavigationDestination.Category_2
							2 -> NavigationDestination.Category_3
							else -> throw IndexOutOfBoundsException("Destination to category: $index")
						}
						
						CategoryItem(
							category = categoryList[index],
							isSelected = (currentRoute == destination) or (currentCategoryRoute == destination),
							todoCount = todoList.filter { return@filter it.categoryID == categoryList[index].id }.size,
							onEditAsDrawerItem = {
								scope.launch {
									scaffoldState.drawerState.close()
								}
								
								val route = "${NavigationDestination.CategoriesScreen}/${categoryList[index].id}"
								navigationController.navigate(route) {
									navigationController.graph.startDestinationRoute?.let { destination ->
										popUpTo(destination) {
											saveState = false
										}
										
										launchSingleTop = true
										restoreState = false
									}
								}
							}
						) {
							currentCategoryRoute = destination
							scope.launch {
								scaffoldState.drawerState.close()
							}
							
							val route = "${NavigationDestination.CategoryScreen}/${categoryList[index].id}"
							navigationController.navigate(route) {
								navigationController.graph.startDestinationRoute?.let { destination ->
									popUpTo(destination) {
										saveState = false
									}
									
									launchSingleTop = true
									restoreState = false
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
								todoCount = run {
									var size = 0
									viewModel.appRepository.todoSize { mSize ->
										size = mSize
									}
									
									size
								},
								isSelected = currentRoute == NavigationDestination.CompleteScreen
							) {
								currentCategoryRoute = ""
								scope.launch {
									scaffoldState.drawerState.close()
								}
								
								navigationController.navigate(NavigationDestination.CompleteScreen) {
									navigationController.graph.startDestinationRoute?.let { destination ->
										popUpTo(destination) {
											saveState = false
										}
										
										launchSingleTop = true
										restoreState = false
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
								
								val route = "${NavigationDestination.CategoriesScreen}/${-1}"
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
					}
					
				}
			},
		) {
			
			NavHost(
				navController = navigationController,
				startDestination = NavigationDestination.DashboardScreen,
			) {
				composable(NavigationDestination.DashboardScreen) {
					DashboardScreen(viewModel)
				}
				
				composable(NavigationDestination.CompleteScreen) {
					CompleteScreen(viewModel)
				}
				
				composable(
					route = "${NavigationDestination.CategoriesScreen}/{categoryID}",
					arguments = listOf(
						navArgument("categoryID") {
							type = NavType.IntType
						}
					)
				) { entry ->
					val categoryID = entry.arguments?.getInt("categoryID") ?: -1
					CategoriesScreen(
						viewModel = viewModel,
						navController = navigationController,
						cID = categoryID
					)
				}
				
				composable(
					route = "${NavigationDestination.CategoryScreen}/{categoryID}",
					arguments = listOf(
						navArgument("categoryID") {
							type = NavType.IntType
						}
					)
				) { entry ->
					val categoryID = entry.arguments?.getInt("categoryID") ?: -1
					if (categoryID != -1) {
						CategoryScreen(
							viewModel = viewModel,
							navController = navigationController,
							categoryID = categoryID
						)
					} else "Category not found".toast(this@MainActivity)
				}
				
				composable(
					route = "${NavigationDestination.EditTodoScreen}/{todoID}",
					arguments = listOf(
						navArgument("todoID") {
							type = NavType.IntType
						}
					)
				) { entry ->
					val todoID = entry.arguments?.getInt("todoID") ?: -1
					if (todoID != -1) {
						EditTodoScreen(
							todoID = todoID,
							viewModel = viewModel,
						)
					} else "Todo not found".toast(this@MainActivity)
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
							isSelected = i == 0
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