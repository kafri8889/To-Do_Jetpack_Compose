package com.anafthdev.todo.utils

import android.os.Handler
import android.os.Looper
import com.anafthdev.todo.database.ApplicationDatabase
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseUtil @Inject constructor(applicationDatabase: ApplicationDatabase) {
	
	private val scope = CoroutineScope(Job() + Dispatchers.IO)
	
	private val databaseDao = applicationDatabase.databaseDAO()
	
	private fun postAction(action: () -> Unit) = Handler(Looper.getMainLooper()).post { action() }
	
	fun getAllTodo(action: (List<Todo>) -> Unit) {
		val todoList = ArrayList<Todo>()
		scope.launch {
			todoList.addAll(databaseDao.getAllTodo())
		}.invokeOnCompletion { postAction {
			action(todoList)
		} }
	}
	
	fun getTodoByCategoryID(categoryID: Int, action: (List<Todo>) -> Unit) {
		val todoList = ArrayList<Todo>()
		scope.launch {
			todoList.addAll(databaseDao.getTodoByCategoryID(categoryID))
		}.invokeOnCompletion { postAction { action(todoList) } }
	}
	
	fun todoSize(action: (Int) -> Unit) {
		var size = 0
		scope.launch {
			size = databaseDao.todoCount()
		}.invokeOnCompletion { postAction { action(size) } }
	}
	
	fun updateTodo(todo: Todo, action: () -> Unit = {}) {
		scope.launch { databaseDao.updateTodo(todo) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun updateTodo(todo: List<Todo>, action: () -> Unit = {}) {
		scope.launch { databaseDao.updateTodo(todo) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun deleteTodoByCategoryID(categoryID: Int, action: () -> Unit) {
		scope.launch { databaseDao.deleteTodoByCategoryID(categoryID) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun deleteTodo(todo: Todo, action: () -> Unit = {}) {
		scope.launch { databaseDao.deleteTodo(todo) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun deleteTodo(todo: List<Todo>, action: () -> Unit = {}) {
		scope.launch { databaseDao.deleteTodo(todo) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun insertTodo(todo: Todo, action: () -> Unit = {}) {
		scope.launch { databaseDao.insertTodo(todo) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun insertTodo(todo: List<Todo>, action: () -> Unit = {}) {
		scope.launch { databaseDao.insertTodo(todo) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	
	
	
	
	fun getAllCategory(action: (List<Category>) -> Unit) {
		val todoList = ArrayList<Category>()
		scope.launch {
			todoList.addAll(databaseDao.getAllCategory())
		}.invokeOnCompletion { postAction {
			action(todoList)
		} }
	}
	
	fun getCategory(id: Int, action: (Category) -> Unit) {
		var category: Category? = null
		scope.launch {
			category = databaseDao.getCategory(id)
		}.invokeOnCompletion { postAction { action(category ?: Category.default) } }
	}
	
	fun categorySize(action: (Int) -> Unit) {
		var size = 0
		scope.launch {
			size = databaseDao.categorySize()
		}.invokeOnCompletion { postAction { action(size) } }
	}
	
	fun updateCategory(category: Category, action: () -> Unit = {}) {
		scope.launch { databaseDao.updateCategory(category) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun updateCategory(category: List<Category>, action: () -> Unit = {}) {
		scope.launch { databaseDao.updateCategory(category) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun deleteCategory(category: Category, action: () -> Unit = {}) {
		scope.launch { databaseDao.deleteCategory(category) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun deleteCategory(category: List<Category>, action: () -> Unit = {}) {
		scope.launch { databaseDao.deleteCategory(category) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun insertCategory(category: Category, action: () -> Unit = {}) {
		scope.launch { databaseDao.insertCategory(category) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
	fun insertCategory(category: List<Category>, action: () -> Unit = {}) {
		scope.launch { databaseDao.insertCategory(category) }.invokeOnCompletion {
			postAction(action)
		}
	}
	
}