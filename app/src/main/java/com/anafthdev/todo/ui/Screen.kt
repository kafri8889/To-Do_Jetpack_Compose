package com.anafthdev.todo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.view_model.AppViewModel

@Composable
fun DashboardScreen(viewModel: AppViewModel) {

}





@Composable
fun CompleteScreen(viewModel: AppViewModel) {

}





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen(viewModel: AppViewModel) {
	viewModel.getAllCategory()
	
	val categories by viewModel.categoryList.observeAsState(initial = emptyList())
	
	Column {
		CategoryItemInput { categoryName, hexColor ->
			viewModel.databaseUtil.insertCategory(Category(categoryName, hexColor)) {
				viewModel.getAllCategory()
			}
		}
		
		LazyColumn {
			items(categories) { category ->
				AnimatedVisibility(
					visible = true,
					enter = expandVertically(),
					exit = shrinkVertically()
				) {
					CategoryItem(
						category = category,
						todoCount = run {
							var todoSize = 0
							viewModel.databaseUtil.todoSizeByCategoryID(category.id) { size ->
								todoSize = size
							}
							
							todoSize
						},
						asDrawerItem = false,
						showMenu = true,
					) {}
				}
			}
		}
	}
}





@Composable
fun CategoryScreen(
	categoryID: Int
) {

}
