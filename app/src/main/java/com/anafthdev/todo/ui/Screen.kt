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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import com.anafthdev.todo.data.CategoryColor
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.utils.AppUtil.toast
import com.anafthdev.todo.utils.ComposeUtil
import com.anafthdev.todo.view_model.AppViewModel
import timber.log.Timber

@Composable
fun DashboardScreen(viewModel: AppViewModel) {

}





@Composable
fun CompleteScreen(viewModel: AppViewModel) {

}





@OptIn(
	ExperimentalFoundationApi::class,
	ExperimentalComposeUiApi::class
)
@Composable
fun CategoriesScreen(
	viewModel: AppViewModel,
	cID: Int? = null
) {
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	var hasNavigate by remember { mutableStateOf(false) }
	
	// used whether in edit mode or not
	var categoryInputModeEdit by remember { mutableStateOf(false) }
	
	val textFieldFocusRequester = remember { FocusRequester() }
	
	// for edit/add category
	var categoryName by remember { mutableStateOf("") }
	var selectedColor by remember { mutableStateOf(CategoryColor.values[0]) }
	var categoryID by remember { mutableStateOf(-1) }
	
	val categories by viewModel.categoryList.observeAsState(initial = emptyList())
	val keyboardState by ComposeUtil.keyboardAsState()
	
	// clear textField focus when keyboard closed
	if (keyboardState == ComposeUtil.Keyboard.Closed) {
		focusManager.clearFocus(true)
	}
	
	if ((cID != null) and (cID != -1) and !hasNavigate) {
		viewModel.databaseUtil.getCategory(cID!!) { category ->
			categoryName = category.name
			selectedColor = category.color
			categoryID = category.id
			categoryInputModeEdit = true
			true.also { hasNavigate = it }
		}
	}
	
	viewModel.getAllCategory()
	
	Column {
		CategoryItemInput(
			name = categoryName,
			color = selectedColor,
			inputModeEdit = categoryInputModeEdit,
			textFieldFocusRequester = textFieldFocusRequester,
			onValueChange = { name, hexColor ->
				categoryName = name
				selectedColor = hexColor
			},
			onDone = { name, hexColor ->
				categoryName = ""
				selectedColor = CategoryColor.values[0]
				if (categoryInputModeEdit) viewModel.updateCategory(Category(name, hexColor, categoryID)) else viewModel.insertCategory(Category(name, hexColor))
				categoryInputModeEdit = false
				categoryID = 0
				keyboardController?.hide()
			},
		)
		
		LazyColumn {
			items(categories) { category ->
				CategoryItem(
					category = category,
					asDrawerItem = false,
					showMenu = true,
					todoCount = run {
						var todoSize = 0
						viewModel.databaseUtil.todoSizeByCategoryID(category.id) { size ->
							todoSize = size
						}
						
						todoSize
					},
					onEdit = {
						categoryID = category.id
						categoryName = category.name
						selectedColor = category.color
						categoryInputModeEdit = true
					},
					onDelete = {
						viewModel.deleteCategory(category)
					}
				) { keyboardController?.hide() }
			}
		}
	}
}





@Composable
fun CategoryScreen(
	viewModel: AppViewModel,
	categoryID: Int
) {

}
