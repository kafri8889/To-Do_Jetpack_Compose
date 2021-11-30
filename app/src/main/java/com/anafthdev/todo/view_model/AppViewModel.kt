package com.anafthdev.todo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo
import com.anafthdev.todo.utils.DatabaseUtil
import javax.inject.Inject

class AppViewModel @Inject constructor(
	val databaseUtil: DatabaseUtil
) {
	
	private val _todoList = MutableLiveData<List<Todo>>()
	val todoList: LiveData<List<Todo>> = _todoList
	
	private val _categoryList = MutableLiveData<List<Category>>()
	val categoryList: LiveData<List<Category>> = _categoryList
	
	fun refresh() {
		databaseUtil.getAllTodo { todo -> _todoList.value = todo }
		databaseUtil.getAllCategory { categories -> _categoryList.value = categories }
	}
	
	fun getAllTodo() = databaseUtil.getAllTodo { todo ->
		_todoList.value = todo
	}
	
	fun insertTodo(todo: Todo) = databaseUtil.insertTodo(todo) {
		getAllTodo()
	}
	
	fun getAllCategory() = databaseUtil.getAllCategory { categories ->
		_categoryList.value = categories
	}
	
	fun insertCategory(category: Category) = databaseUtil.insertCategory(category) {
		getAllCategory()
	}
	
	fun updateCategory(category: Category) = databaseUtil.updateCategory(category) {
		getAllCategory()
	}
	
	fun deleteCategory(category: Category) = databaseUtil.deleteCategory(category) {
		getAllCategory()
	}
}