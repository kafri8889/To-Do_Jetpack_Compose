package com.anafthdev.todo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.ui.theme.on_surface_light

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
		CategoryItemInput(
			name = "",
			color = 0L,
			textFieldFocusRequester = FocusRequester(),
			onValueChange = { categoryName, hexColor -> },
			onDone = { categoryName, hexColor -> }
		)
		
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
	Column {
		TodoItemInputPreview()
	}
}