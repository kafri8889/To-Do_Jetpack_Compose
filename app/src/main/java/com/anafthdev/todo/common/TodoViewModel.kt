package com.anafthdev.todo.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo
import javax.inject.Inject

class TodoViewModel @Inject constructor(
	val repository: RepositoryI
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
		repository.getAllTodo { todo -> _todoList.value = todo }
		repository.getAllCategory { categories -> _categoryList.value = categories }
	}
	
	fun getAllTodo() = repository.getAllTodo { todo ->
		_todoList.value = todo
	}
	
	fun updateTodo(todo: Todo) = repository.updateTodo(todo) {
		getAllTodo()
	}
	
	fun deleteTodo(todo: Todo) = repository.deleteTodo(todo) {
		getAllTodo()
	}
	
	private fun deleteTodoByCategoryID(categoryID: Int) = repository.deleteTodoByCategoryID(categoryID) {
		getAllTodo()
	}
	
	fun insertTodo(todo: Todo) = repository.insertTodo(todo) {
		getAllTodo()
	}
	
	/**
	 * get To-do List by categoryID
	 */
	fun getTodoListByID(id: Int) {
		repository.getTodoByCategoryID(id) { list ->
			_todoListByID.value = list
		}
	}
	
	fun getAllCategory() = repository.getAllCategory { categories ->
		_categoryList.value = categories
	}
	
	fun insertCategory(category: Category) = repository.insertCategory(category) {
		getAllCategory()
	}
	
	fun updateCategory(category: Category) = repository.updateCategory(category) {
		getAllCategory()
	}
	
	fun deleteCategory(category: Category) = repository.deleteCategory(category) {
		deleteTodoByCategoryID(category.id)
		getAllCategory()
	}
}