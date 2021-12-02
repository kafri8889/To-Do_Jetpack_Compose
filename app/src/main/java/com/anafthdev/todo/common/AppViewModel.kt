package com.anafthdev.todo.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo
import com.anafthdev.todo.utils.DatabaseUtil
import javax.inject.Inject

class AppViewModel @Inject constructor(
	val appRepository: AppRepositoryI
): ViewModel() {
	
	private val _todoList = MutableLiveData<List<Todo>>()
	val todoList: LiveData<List<Todo>> = _todoList
	
	/**
	 * get To-do list by id, see setTodoByID(id: Int)
	 */
	private val _todoListByID = MutableLiveData<List<Todo>>()
	val todoListByID: LiveData<List<Todo>> = _todoListByID
	
	private val _categoryList = MutableLiveData<List<Category>>()
	val categoryList: LiveData<List<Category>> = _categoryList
	
	fun refresh() {
		appRepository.getAllTodo { todo -> _todoList.value = todo }
		appRepository.getAllCategory { categories -> _categoryList.value = categories }
	}
	
	fun getAllTodo() = appRepository.getAllTodo { todo ->
		_todoList.value = todo
	}
	
//	fun updateTodo(todo: Todo) = appRepository.updateTodo(todo) {
//		getAllTodo()
//	}
	
	fun insertTodo(todo: Todo) = appRepository.insertTodo(todo) {
		getAllTodo()
	}
	
	/**
	 * get To-do List by categoryID
	 */
	fun getTodoListByID(id: Int) {
		appRepository.getTodoByCategoryID(id) { list ->
			_todoListByID.value = list
		}
	}
	
	fun getAllCategory() = appRepository.getAllCategory { categories ->
		_categoryList.value = categories
	}
	
	fun insertCategory(category: Category) = appRepository.insertCategory(category) {
		getAllCategory()
	}
	
	fun updateCategory(category: Category) = appRepository.updateCategory(category) {
		getAllCategory()
	}
	
	fun deleteCategory(category: Category) = appRepository.deleteCategory(category) {
		getAllCategory()
	}
}