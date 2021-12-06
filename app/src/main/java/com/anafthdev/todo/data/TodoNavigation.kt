package com.anafthdev.todo.data

object TodoNavigation {
	
	sealed class Navigation(val destination: String, var route: String) {
		
		object DashboardScreen: Navigation(
			destination = TodoNavigation.DashboardScreen,
			route = TodoNavigation.DashboardScreen
		)
		
		object CompleteScreen: Navigation(
			destination = TodoNavigation.CompleteScreen,
			route = TodoNavigation.CompleteScreen
		)
		
		object EditTodoScreen: Navigation(
			destination = TodoNavigation.EditTodoScreen,
			route = "${TodoNavigation.EditTodoScreen}/{todoID}"
		)
		
		object CategoriesScreen: Navigation(
			destination = TodoNavigation.CategoriesScreen,
			route = "${TodoNavigation.CategoriesScreen}/{categoryID}"
		)
		
		object CategoryScreen: Navigation(
			destination = TodoNavigation.CategoryScreen,
			route = "${TodoNavigation.CategoryScreen}/{categoryID}"
		)
		
		object Category_1: Navigation(
			destination = TodoNavigation.Category_1,
			route = "${TodoNavigation.Category_1}/{categoryID}"
		)
		
		object Category_2: Navigation(
			destination = TodoNavigation.Category_2,
			route = "${TodoNavigation.Category_2}/{categoryID}"
		)
		
		object Category_3: Navigation(
			destination = TodoNavigation.Category_3,
			route = "${TodoNavigation.Category_3}/{categoryID}"
		)
		
	}
	
	const val DashboardScreen = "Dashboard"
	
	const val CategoryScreen = "Category"
	
	const val Category_1 = "Category 1"
	
	const val Category_2 = "Category 2"
	
	const val Category_3 = "Category 3"
	
	const val CategoriesScreen = "Categories"
	
	const val CompleteScreen = "Complete"
	
	const val EditTodoScreen = "Edit Todo"
}