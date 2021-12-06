package com.anafthdev.todo.ui

import android.app.DatePickerDialog
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.anafthdev.todo.common.Repository
import com.anafthdev.todo.common.TodoViewModel
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo
import com.anafthdev.todo.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Preview(showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
	val fakeViewModel = TodoViewModel(Repository.FakeRepository())
	
	Column {
		
		TodoItemInput(
			todoName = "",
			categoryID = Category.default.id,
			textFieldFocusRequester = FocusRequester(),
			viewModel = fakeViewModel,
			rotationAngleArrowIcon = 0f,
			onValueChange = {},
			onDone = { title, timeInMillis, categoryID -> },
			onClick = {}
		)
		
		
		
		LazyColumn {
			items(2) { index ->
				TodoItem(
					todo = Todo.todo_sample,
					isTodoComplete = index == 1,
					viewModel = fakeViewModel,
					onCheckboxValueChange = {},
					onClick = {}
				)
			}
		}
	}
}





//@Preview(showSystemUi = true)
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
fun CategoryScreenPreview() {
	Column {
		TodoItemInputPreview()
	}
}





@OptIn(
	ExperimentalUnitApi::class,
	ExperimentalMaterialApi::class
)
@Preview(showSystemUi = true)
@Composable
fun EditTodoScreenPreview() {
	
	val todo = Todo.todo_sample
	val category = Category.default
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
	) {
		
		// Checkbox and To-Do title
		Row(verticalAlignment = Alignment.CenterVertically) {
			Checkbox(
				checked = true,
				onCheckedChange = {},
				modifier = Modifier
					.padding(start = 16.dp, end = 16.dp)
			)
			
			Text(
				text = todo.title,
				color = black.copy(alpha = 0.8f),
				fontWeight = FontWeight.SemiBold,
				fontSize = TextUnit(16f, TextUnitType.Sp),
				textDecoration = TextDecoration.LineThrough
			)
		} // Checkbox and To-Do title
		
		
		
		Column(
			modifier = Modifier
				.padding(start = 16.dp, end = 16.dp, top = 16.dp)
				.clip(RoundedCornerShape(8.dp))
				.background(gray.copy(alpha = 0.08f))
		) {
			
			// Category
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 4.dp)
			) {
				
				Text(
					text = "Category",
					color = gray,
					fontSize = TextUnit(14f, TextUnitType.Sp),
					fontWeight = FontWeight.SemiBold,
					modifier = Modifier
						.wrapContentSize(Alignment.CenterStart)
				)
				
				Spacer(modifier = Modifier.weight(1f))
				
				Card(
					elevation = 2.dp,
					backgroundColor = surface_light,
					shape = RoundedCornerShape(8.dp),
					onClick = {},
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Center,
						modifier = Modifier
							.size(width = 120.dp, height = 32.dp)
					
					) {
						Icon(
							painter = painterResource(id = com.anafthdev.todo.R.drawable.ic_rect),
							contentDescription = null,
							tint = Color(category.color),
							modifier = Modifier
								.size(22.dp)
								.padding(4.dp)
						)
						
						
						
						Text(
							text = category.name,
							textAlign = TextAlign.Center,
							fontSize = TextUnit(12f, TextUnitType.Sp),
							fontWeight = FontWeight.Bold,
							color = black.copy(alpha = 0.8f),
							overflow = TextOverflow.Ellipsis,
							modifier = Modifier
								.padding(start = 2.dp, end = 2.dp)
						)
						
						
						
						Icon(
							imageVector = Icons.Default.KeyboardArrowDown,
							contentDescription = null,
							tint = black.copy(alpha = 0.8f),
							modifier = Modifier
								.size(28.dp)
								.padding(start = 4.dp, end = 4.dp, top = 4.dp)
						)
					}
				}
			}  // Category
			
			
			
			// Date
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 14.dp)
			) {
				
				Text(
					text = "Due Date",
					color = gray,
					fontSize = TextUnit(14f, TextUnitType.Sp),
					fontWeight = FontWeight.SemiBold,
					modifier = Modifier
						.wrapContentSize(Alignment.CenterStart)
				)
				
				Spacer(modifier = Modifier.weight(1f))
				
				Card(
					elevation = 2.dp,
					backgroundColor = surface_light,
					shape = RoundedCornerShape(8.dp),
					onClick = {},
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Center,
						modifier = Modifier
							.size(width = 120.dp, height = 32.dp)
					) {
						Text(
							text = "Select Date",
							textAlign = TextAlign.Center,
							fontSize = TextUnit(12f, TextUnitType.Sp),
							fontWeight = FontWeight.Bold,
							color = black.copy(alpha = 0.8f),
							overflow = TextOverflow.Ellipsis,
							modifier = Modifier
								.padding(start = 2.dp, end = 2.dp)
						)
						
						
						
						Icon(
							imageVector = Icons.Default.KeyboardArrowDown,
							contentDescription = null,
							tint = black.copy(alpha = 0.8f),
							modifier = Modifier
								.size(28.dp)
								.padding(start = 4.dp, end = 4.dp, top = 4.dp)
						)
					}
				}
			}  // Date
		}
		
		
		
		Card(
			elevation = 2.dp,
			backgroundColor = bianca,
			shape = RoundedCornerShape(8.dp),
			onClick = {
			
			},
			modifier = Modifier
				.wrapContentSize()
				.padding(start = 16.dp, end = 16.dp, top = 16.dp)
		) {
			OutlinedTextField(
				value = todo.content,
				onValueChange = {},
				textStyle = TextStyle(
					fontSize = TextUnit(14f, TextUnitType.Sp)
				),
				placeholder = {
					Text("Write a note...")
				},
				colors = TextFieldDefaults.outlinedTextFieldColors(
					textColor = buttercup.copy(alpha = 0.8f),
					focusedBorderColor = Color.Transparent,
					unfocusedBorderColor = Color.Transparent
				),
				modifier = Modifier
					.fillMaxWidth()
					.height(288.dp)
			)
		}
		
		
		
	}  // Main Content
}
