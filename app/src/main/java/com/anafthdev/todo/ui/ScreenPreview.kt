package com.anafthdev.todo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anafthdev.todo.data.CategoryColor
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.view_model.AppViewModel

@Preview(showSystemUi = true)
@Composable
fun DashboardScreenPreview() {

}





@Preview(showSystemUi = true)
@Composable
fun CompleteScreenPreview() {
	val categories = listOf(
		Category(
			"Health",
			0xFF904D00
		),
	
		Category(
			"Personal",
			0xFF745943
		)
	)
	
	Column {
//		CategoryItemInput { categoryName, hexColor -> }
		LazyColumn {
			items(categories) { category ->
				CategoryItem(
					category = category,
					todoCount = 0,
					asDrawerItem = false
				) {}
			}
		}
	}
}





@Preview(showSystemUi = true)
@Composable
fun CategoriesScreenPreview() {

}





@Preview(showSystemUi = true)
@Composable
fun CategoryScreenPreview() {

}