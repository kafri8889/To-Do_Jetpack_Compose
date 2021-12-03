package com.anafthdev.todo.common

import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo

interface AppRepositoryI {
	
	fun getAllTodo(action: (List<Todo>) -> Unit)
	
	fun getTodoByCategoryID(categoryID: Int, action: (List<Todo>) -> Unit)
	
	fun todoSize(action: (Int) -> Unit)
	
	fun updateTodo(todo: Todo, action: () -> Unit = {})
	
	fun updateTodo(todo: List<Todo>, action: () -> Unit = {})
	
	fun deleteTodoByCategoryID(categoryID: Int, action: () -> Unit)
	
	fun deleteTodo(todo: Todo, action: () -> Unit = {})
	
	fun deleteTodo(todo: List<Todo>, action: () -> Unit = {})
	
	fun insertTodo(todo: Todo, action: () -> Unit = {})
	
	fun insertTodo(todo: List<Todo>, action: () -> Unit= {})
	
	
	
	fun getAllCategory(action: (List<Category>) -> Unit)
	
	fun getCategory(id: Int, action: (Category) -> Unit)
	
	fun categorySize(action: (Int) -> Unit)
	
	fun updateCategory(category: Category, action: () -> Unit= {})
	
	fun updateCategory(category: List<Category>, action: () -> Unit = {})
	
	fun deleteCategory(category: Category, action: () -> Unit = {})
	
	fun deleteCategory(category: List<Category>, action: () -> Unit = {})
	
	fun insertCategory(category: Category, action: () -> Unit = {})
	
	fun insertCategory(category: List<Category>, action: () -> Unit = {})
	
}