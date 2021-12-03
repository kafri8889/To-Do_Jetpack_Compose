package com.anafthdev.todo.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.anafthdev.todo.data.CategoryColor
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.ui.theme.on_surface_light
import com.anafthdev.todo.utils.ComposeUtil
import com.anafthdev.todo.common.AppViewModel
import com.anafthdev.todo.data.NavigationDestination
import com.anafthdev.todo.model.Todo
import com.anafthdev.todo.ui.theme.surface_light
import com.anafthdev.todo.utils.AppUtil.toast
import kotlinx.coroutines.launch

@OptIn(
	ExperimentalComposeUiApi::class,
	ExperimentalMaterialApi::class
)
@Composable
fun DashboardScreen(viewModel: AppViewModel) {
	
	val context = LocalContext.current
	val keyboardController = LocalSoftwareKeyboardController.current
	val focusManager = LocalFocusManager.current
	
	val keyboardState by ComposeUtil.keyboardAsState()
	val todoList by viewModel.todoList.observeAsState(initial = emptyList())
	val categoryList by viewModel.categoryList.observeAsState(initial = emptyList())
	
	val scope = rememberCoroutineScope()
	val scaffoldState = rememberBottomSheetScaffoldState(
		bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
	)
	
	var todoName by remember { mutableStateOf("") }
	var selectedCategoryID by remember { mutableStateOf(Category.default.id) }
	val textFieldFocusRequester = remember { FocusRequester() }
	
	// clear textField focus when keyboard closed
	if (keyboardState == ComposeUtil.Keyboard.Closed) {
		focusManager.clearFocus(force = true)
	}
	
	BottomSheetScaffold(
		scaffoldState = scaffoldState,
		sheetBackgroundColor = surface_light,
		sheetPeekHeight = 0.dp,
		sheetElevation = 8.dp,
		sheetShape = RoundedCornerShape(24.dp),
		sheetContent = {
			LazyColumn(
				modifier = Modifier
					.fillMaxWidth()
					.height(384.dp)
			) {
				items(categoryList) { category ->
					SelectCategoryItem(
						category = category,
						onClick = {
							selectedCategoryID = category.id
							scope.launch {
								scaffoldState.bottomSheetState.collapse()
							}
						}
					)
				}
			}
		},
	) {
		Column {
			
			TodoItemInput(
				todoName = todoName,
				categoryID = selectedCategoryID,
				textFieldFocusRequester = textFieldFocusRequester,
				viewModel = viewModel,
				rotationAngleArrowIcon = run {
					if (scaffoldState.bottomSheetState.isExpanded) 180f
					else -360f
				},
				onValueChange = { name ->
					todoName = name
				},
				onDone = { title, timeInMillis, categoryID ->
					// Save To-Do
					if (title.isNotBlank()) {
						viewModel.insertTodo(Todo(
							title = title,
							content = "",
							date = timeInMillis,
							dateCreated = System.currentTimeMillis(),
							categoryID = categoryID,
							checkboxes = emptyList()
						))
						
						todoName = ""
					}
					
					keyboardController?.hide()
				},
				onClick = {
					if (scaffoldState.bottomSheetState.isExpanded) {
						scope.launch { scaffoldState.bottomSheetState.collapse() }
					} else scope.launch { scaffoldState.bottomSheetState.expand() }
				}
			)
			
			
			
			LazyColumn {
				items(todoList) { todo ->
					TodoItem(
						todo = todo,
						isTodoComplete = todo.isComplete,
						viewModel = viewModel,
						onCheckboxValueChange = {},
						onClick = {
						
						}
					)
				}
			}
		}
	}
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
	navController: NavHostController,
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
	
	val todoList by viewModel.todoList.observeAsState(initial = emptyList())
	val categories by viewModel.categoryList.observeAsState(initial = emptyList())
	val keyboardState by ComposeUtil.keyboardAsState()
	
	// clear textField focus when keyboard closed
	if (keyboardState == ComposeUtil.Keyboard.Closed) {
		focusManager.clearFocus(force = true)
	}
	
	if ((cID != null) and (cID != -1) and !hasNavigate) {
		viewModel.appRepository.getCategory(cID!!) { category ->
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
					todoCount = todoList.filter { return@filter it.categoryID == category.id }.size,
					onEdit = {
						categoryID = category.id
						categoryName = category.name
						selectedColor = category.color
						categoryInputModeEdit = true
					},
					onDelete = {
						viewModel.deleteCategory(category)
					}
				) {
					keyboardController?.hide()
					
					val route = "${NavigationDestination.CategoryScreen}/${category.id}"
					navController.navigate(route) {
						navController.graph.startDestinationRoute?.let { destination ->
							popUpTo(destination) {
								saveState = false
							}
							
							launchSingleTop = true
							restoreState = false
						}
					}
				}
			}
		}
	}
}





@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryScreen(
	viewModel: AppViewModel,
	navController: NavHostController,
	categoryID: Int
) {
	viewModel.getTodoListByID(categoryID)
	
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val todoList by viewModel.todoListByID.observeAsState(initial = emptyList())
	val keyboardState by ComposeUtil.keyboardAsState()
	
	var todoName by remember { mutableStateOf("") }
	var category by remember { mutableStateOf(Category.default) }
	val textFieldFocusRequester = remember { FocusRequester() }
	var hasNavigate by remember { mutableStateOf(false) }
	
	// clear textField focus when keyboard closed
	if (keyboardState == ComposeUtil.Keyboard.Closed) {
		focusManager.clearFocus(force = true)
	}
	
	if (!hasNavigate) {
		viewModel.appRepository.getCategory(categoryID) { mCategory ->
			category = mCategory
		}
		
		true.also { hasNavigate = it }
	}
	
	Column {
		TodoItemInput(
			todoName = todoName,
			viewModel = viewModel,
			category = category,
			textFieldFocusRequester = textFieldFocusRequester,
			onValueChange = { name ->
				todoName = name
			},
			onDone = { title, timeInMillis, categoryID ->
				// Save To-Do
				if (title.isNotBlank()) {
					viewModel.insertTodo(Todo(
						title = title,
						content = "",
						date = timeInMillis,
						dateCreated = System.currentTimeMillis(),
						categoryID = categoryID,
						checkboxes = emptyList()
					))
					
					todoName = ""
				}
				
				keyboardController?.hide()
			}
		)
		
		LazyColumn {
			items(todoList) { todo ->
				var isTodoComplete by remember { mutableStateOf(todo.isComplete) }
				TodoItem(
					todo = todo,
					viewModel = viewModel,
					isTodoComplete = isTodoComplete,
					onClick = {
						val route = "${NavigationDestination.EditTodoScreen}/${todo.id}"
						navController.navigate(route) {
							navController.graph.startDestinationRoute?.let { destination ->
								popUpTo(destination) {
									saveState = false
								}
								
								launchSingleTop = true
								restoreState = false
							}
						}
					},
					onCheckboxValueChange = { mIsTodoComplete ->
						isTodoComplete = mIsTodoComplete
						todo.isComplete = mIsTodoComplete
						viewModel.appRepository.updateTodo(todo) {
							viewModel.getTodoListByID(categoryID)
						}
					}
				)
			}
		}
	}
}


@Composable
fun EditTodoScreen(
	todoID: Int,
	viewModel: AppViewModel
) {

}
