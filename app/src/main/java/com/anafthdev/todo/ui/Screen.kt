package com.anafthdev.todo.ui

import android.app.DatePickerDialog
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.anafthdev.todo.R
import com.anafthdev.todo.common.TodoViewModel
import com.anafthdev.todo.data.CategoryColor
import com.anafthdev.todo.data.TodoNavigation
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo
import com.anafthdev.todo.ui.theme.*
import com.anafthdev.todo.utils.AppUtil.get
import com.anafthdev.todo.utils.ComposeUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@OptIn(
	ExperimentalComposeUiApi::class,
	ExperimentalMaterialApi::class
)
@Composable
fun DashboardScreen(
	navController: NavHostController,
	viewModel: TodoViewModel
) {
	
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
	
	var showTodoPopupMenu by remember { mutableStateOf(false) }
	var todoToDelete by remember { mutableStateOf(Todo.todo_sample) }
	
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
			
			
			
			if (showTodoPopupMenu) {
				Popup(
					alignment = Alignment.TopEnd,
					onDismissRequest = {
						showTodoPopupMenu = false
					}
				) {
					Card(
						elevation = 4.dp,
						shape = RoundedCornerShape(8.dp),
						onClick = {
							viewModel.deleteTodo(todoToDelete)
							showTodoPopupMenu = false
						},
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.padding(8.dp)
						) {
							Icon(
								imageVector = Icons.Default.Delete,
								contentDescription = null,
								tint = black.copy(alpha = 0.8f),
								modifier = Modifier.padding(start = 8.dp)
							)
							
							Text(
								text = "Delete",
								color = black.copy(alpha = 0.8f)
							)
						}
					}
				}
			}
			
			
			
			LazyColumn {
				items(todoList) { todo ->
					var isTodoComplete by remember { mutableStateOf(todo.isComplete) }
					
					TodoItem(
						todo = todo,
						isTodoComplete = isTodoComplete,
						viewModel = viewModel,
						onCheckboxValueChange = { mIsTodoComplete ->
							isTodoComplete = mIsTodoComplete
							todo.isComplete = mIsTodoComplete
							viewModel.repository.updateTodo(todo) {
								viewModel.getAllTodo()
							}
						},
						onClick = {
							val route = "${TodoNavigation.EditTodoScreen}/${todo.id}"
							navController.navigate(route) {
								navController.graph.startDestinationRoute?.let {
									popUpTo(TodoNavigation.Navigation.DashboardScreen.route) {
										saveState = false
									}
									
									launchSingleTop = true
									restoreState = false
								}
							}
						},
						onLongClick = {
							todoToDelete = todo
							showTodoPopupMenu = true
						},
					)
					
				}
			}
		}
	}
}





@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompleteScreen(
	navController: NavHostController,
	viewModel: TodoViewModel
) {
	
	val todoList by viewModel.todoList.observeAsState(initial = emptyList())
	
	var showTodoPopupMenu by remember { mutableStateOf(false) }
	var todoToDelete by remember { mutableStateOf(Todo.todo_sample) }
	
	Column {
		
		if (showTodoPopupMenu) {
			Popup(
				alignment = Alignment.TopEnd,
				onDismissRequest = {
					showTodoPopupMenu = false
				}
			) {
				Card(
					elevation = 4.dp,
					shape = RoundedCornerShape(8.dp),
					onClick = {
						viewModel.deleteTodo(todoToDelete)
						showTodoPopupMenu = false
					},
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.padding(8.dp)
					) {
						Icon(
							imageVector = Icons.Default.Delete,
							contentDescription = null,
							tint = black.copy(alpha = 0.8f),
							modifier = Modifier.padding(start = 8.dp)
						)
						
						Text(
							text = "Delete",
							color = black.copy(alpha = 0.8f)
						)
					}
				}
			}
		}
		
		LazyColumn {
			items(todoList.filter { it.isComplete }) { todo ->
				var isTodoComplete by remember { mutableStateOf(todo.isComplete) }
				
				TodoItem(
					todo = todo,
					isTodoComplete = isTodoComplete,
					viewModel = viewModel,
					onCheckboxValueChange = { mIsTodoComplete ->
						isTodoComplete = mIsTodoComplete
						todo.isComplete = mIsTodoComplete
						viewModel.repository.updateTodo(todo) {
							viewModel.getAllTodo()
						}
					},
					onClick = {
						val route = "${TodoNavigation.EditTodoScreen}/${todo.id}"
						navController.navigate(route) {
							navController.graph.startDestinationRoute?.let {
								popUpTo(TodoNavigation.Navigation.CompleteScreen.route) {
									saveState = false
								}
								
								launchSingleTop = true
								restoreState = false
							}
						}
					},
					onLongClick = {
						todoToDelete = todo
						showTodoPopupMenu = true
					}
				)
			}
		}
	}
}





@OptIn(
	ExperimentalFoundationApi::class,
	ExperimentalComposeUiApi::class
)
@Composable
fun CategoriesScreen(
	viewModel: TodoViewModel,
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
		viewModel.repository.getCategory(cID!!) { category ->
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
			onFocusChange = { focusState ->
				// exit edit mode when category name is blank and text field not focused
				if (categoryName.isBlank() and !focusState.isFocused) {
					categoryInputModeEdit = false
					selectedColor = CategoryColor.values[0]
				}
			}
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
					
					val route = "${TodoNavigation.CategoryScreen}/${category.id}"
					navController.navigate(route) {
						navController.graph.startDestinationRoute?.let {
							popUpTo(TodoNavigation.Navigation.CategoriesScreen.route) {
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





@OptIn(
	ExperimentalComposeUiApi::class,
	ExperimentalMaterialApi::class,
)
@Composable
fun CategoryScreen(
	viewModel: TodoViewModel,
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
	
	var showTodoPopupMenu by remember { mutableStateOf(false) }
	var todoToDelete by remember { mutableStateOf(Todo.todo_sample) }
	
	val textFieldFocusRequester = remember { FocusRequester() }
	var hasNavigate by remember { mutableStateOf(false) }
	
	// clear textField focus when keyboard closed
	if (keyboardState == ComposeUtil.Keyboard.Closed) {
		focusManager.clearFocus(force = true)
	}
	
	if (!hasNavigate) {
		viewModel.repository.getCategory(categoryID) { mCategory ->
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
		
		
		
		if (showTodoPopupMenu) {
			Popup(
				alignment = Alignment.TopEnd,
				onDismissRequest = {
					showTodoPopupMenu = false
				}
			) {
				Card(
					elevation = 4.dp,
					shape = RoundedCornerShape(8.dp),
					onClick = {
						viewModel.deleteTodo(todoToDelete)
						showTodoPopupMenu = false
					},
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.padding(8.dp)
					) {
						Icon(
							imageVector = Icons.Default.Delete,
							contentDescription = null,
							tint = black.copy(alpha = 0.8f),
							modifier = Modifier.padding(start = 8.dp)
						)
						
						Text(
							text = "Delete",
							color = black.copy(alpha = 0.8f)
						)
					}
				}
			}
		}
		
		
		
		LazyColumn {
			items(todoList) { todo ->
				var isTodoComplete by remember { mutableStateOf(todo.isComplete) }
				
				TodoItem(
					todo = todo,
					viewModel = viewModel,
					isTodoComplete = isTodoComplete,
					onClick = {
						val route = "${TodoNavigation.EditTodoScreen}/${todo.id}"
						navController.navigate(route) {
							navController.graph.startDestinationRoute?.let {
								popUpTo(TodoNavigation.Navigation.CategoriesScreen.route) {
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
						viewModel.repository.updateTodo(todo) {
							viewModel.getTodoListByID(categoryID)
						}
					},
					onLongClick = {
						todoToDelete = todo
						showTodoPopupMenu = true
					}
				)
				
			}
		}
	}
}


@OptIn(
	ExperimentalUnitApi::class,
	ExperimentalComposeUiApi::class,
	ExperimentalMaterialApi::class
)
@Composable
fun EditTodoScreen(
	todoID: Int,
	viewModel: TodoViewModel,
) {
	
	val context = LocalContext.current
	
	val categoryList by viewModel.categoryList.observeAsState(initial = emptyList())
	val scope = rememberCoroutineScope()
	val scaffoldState = rememberBottomSheetScaffoldState(
		bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
	)
	
	var todo by remember { mutableStateOf(Todo.todo_sample) }
	var title by remember { mutableStateOf(todo.title) }
	var content by remember { mutableStateOf(todo.content) }
	var date by remember { mutableStateOf(todo.date) }
	var isComplete by remember { mutableStateOf(todo.isComplete) }
	var selectedCategoryID by remember { mutableStateOf(todo.categoryID) }
	var todoCheckboxes by remember { mutableStateOf(todo.checkboxes) }
	var currentCategory by remember { mutableStateOf(Category.default) }
	
	var currentFocusedCheckboxTitle by remember { mutableStateOf("") }
	
	var hasNavigate by remember { mutableStateOf(false) }
	
	if (!hasNavigate) {
		viewModel.repository.getTodo(todoID) { mTodo ->
			todo = mTodo
			title = todo.title
			content = todo.content
			date = todo.date
			isComplete = todo.isComplete
			selectedCategoryID = todo.categoryID
			todoCheckboxes = mTodo.checkboxes
			
			viewModel.repository.getCategory(selectedCategoryID) { mCategory ->
				currentCategory = mCategory
			}
		}
		
		true.also { hasNavigate = it }
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
							todo.categoryID = selectedCategoryID
							currentCategory = categoryList.get { it.id == selectedCategoryID } ?: Category.default
							viewModel.updateTodo(todo)
							scope.launch {
								scaffoldState.bottomSheetState.collapse()
							}
						}
					)
				}
			}
		},
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
		) {
			
			// Checkbox and To-Do title
			Row(verticalAlignment = Alignment.CenterVertically) {
				Checkbox(
					checked = isComplete,
					onCheckedChange = { isChecked ->
						isComplete = isChecked
						todo.isComplete = isComplete
						viewModel.updateTodo(todo)
					},
					modifier = Modifier
						.padding(start = 16.dp, end = 16.dp)
				)
				
				Text(
					text = title,
					color = black.copy(alpha = 0.8f),
					fontWeight = FontWeight.SemiBold,
					fontSize = TextUnit(16f, TextUnitType.Sp),
					textDecoration = if (isComplete) TextDecoration.LineThrough else TextDecoration.None
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
						onClick = {
							scope.launch {
								if (scaffoldState.bottomSheetState.isExpanded) {
									scaffoldState.bottomSheetState.collapse()
								} else scaffoldState.bottomSheetState.expand()
							}
						},
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.Center,
							modifier = Modifier
								.size(width = 120.dp, height = 32.dp)
						
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_rect),
								contentDescription = null,
								tint = Color(currentCategory.color),
								modifier = Modifier
									.size(22.dp)
									.padding(4.dp)
							)
							
							
							
							Text(
								text = currentCategory.name,
								textAlign = TextAlign.Center,
								fontSize = TextUnit(12f, TextUnitType.Sp),
								fontWeight = FontWeight.Bold,
								color = black.copy(alpha = 0.8f),
								overflow = TextOverflow.Ellipsis,
								modifier = Modifier
									.padding(start = 2.dp, end = 2.dp)
							)
							
							
							
							val rotationAngle by animateFloatAsState(
								targetValue = run {
									if (scaffoldState.bottomSheetState.isExpanded) 180f
									else -360f
								},
								animationSpec = tween(400)
							)
							
							Icon(
								imageVector = Icons.Default.KeyboardArrowDown,
								contentDescription = null,
								tint = black.copy(alpha = 0.8f),
								modifier = Modifier
									.size(28.dp)
									.padding(start = 4.dp, end = 4.dp, top = 4.dp)
									.rotate(rotationAngle)
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
						onClick = {
							val calendar = Calendar.getInstance()
							val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
								calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
								calendar[Calendar.MONTH] = month
								calendar[Calendar.YEAR] = year
								
								date = calendar.timeInMillis
								todo.date = date
								viewModel.updateTodo(todo)
							}
							
							DatePickerDialog(
								context,
								listener,
								calendar[Calendar.YEAR],
								calendar[Calendar.MONTH],
								calendar[Calendar.DAY_OF_MONTH]
							).show()
						},
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.Center,
							modifier = Modifier
								.size(width = 120.dp, height = 32.dp)
						) {
							Text(
								text = run {
									if (date == 0L) "Select Date"
									else SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(date)
								},
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
					value = content,
					onValueChange = { s ->
						content = s
						todo.content = content
						viewModel.updateTodo(todo)
					},
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
			
			
			
			TodoCheckboxes(
				value = currentFocusedCheckboxTitle,
				todoCheckboxes = todoCheckboxes,
				onCheckedChange = { id, isChecked ->
					todo.checkboxes = ArrayList(todoCheckboxes).apply {
						get { it.id == id }!!.isComplete = isChecked
					}
					
					todo.checkboxes = todoCheckboxes
					viewModel.repository.updateTodo(todo) {
						viewModel.repository.getTodo(todoID) { mTodo ->
							todo = mTodo
						}
					}
				},
				onDelete = { id ->
					todoCheckboxes = ArrayList(todoCheckboxes).apply {
						remove(todoCheckboxes.get { it.id == id }!!)
					}
					
					todo.checkboxes = todoCheckboxes
					viewModel.repository.updateTodo(todo) {
						viewModel.repository.getTodo(todoID) { mTodo ->
							todo = mTodo
						}
					}
				},
				onFocusChange = { s ->
					currentFocusedCheckboxTitle = s
				},
				onValueChange = { id, s ->
					currentFocusedCheckboxTitle = s
					
					todo.checkboxes = ArrayList(todoCheckboxes).apply {
						get { it.id == id }!!.title = currentFocusedCheckboxTitle
					}
					
					todo.checkboxes = todoCheckboxes
					viewModel.repository.updateTodo(todo) {
						viewModel.repository.getTodo(todoID) { mTodo ->
							todo = mTodo
						}
					}
				},
				onNewItem = { todoCheckbox ->
					todoCheckboxes = ArrayList(todoCheckboxes).apply {
						add(todoCheckbox)
					}
					
					todo.checkboxes = todoCheckboxes
					viewModel.repository.updateTodo(todo) {
						viewModel.repository.getTodo(todoID) { mTodo ->
							todo = mTodo
						}
					}
				},
				modifier = Modifier
					.padding(start = 8.dp, end = 8.dp, top = 16.dp)
			)
			
		}  // Main Content
	}  // Bottom Sheet Scaffold
}
