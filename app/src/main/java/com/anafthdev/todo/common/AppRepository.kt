package com.anafthdev.todo.common

import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo
import com.anafthdev.todo.utils.DatabaseUtil
import javax.inject.Inject

class AppRepository @Inject constructor(
	private val databaseUtil: DatabaseUtil
) : AppRepositoryI {
	override fun getAllTodo(action: (List<Todo>) -> Unit) {
		databaseUtil.getAllTodo(action)
	}
	
	override fun getTodoByCategoryID(categoryID: Int, action: (List<Todo>) -> Unit) {
		databaseUtil.getTodoByCategoryID(categoryID, action)
	}
	
	override fun todoSize(action: (Int) -> Unit) {
		databaseUtil.todoSize(action)
	}
	
	override fun updateTodo(todo: Todo, action: () -> Unit) {
		databaseUtil.updateTodo(todo, action)
	}
	
	override fun updateTodo(todo: List<Todo>, action: () -> Unit) {
		databaseUtil.updateTodo(todo, action)
	}
	
	override fun deleteTodoByCategoryID(categoryID: Int, action: () -> Unit) {
		databaseUtil.deleteTodoByCategoryID(categoryID, action)
	}
	
	override fun deleteTodo(todo: Todo, action: () -> Unit) {
		databaseUtil.deleteTodo(todo, action)
	}
	
	override fun deleteTodo(todo: List<Todo>, action: () -> Unit) {
		databaseUtil.deleteTodo(todo, action)
	}
	
	override fun insertTodo(todo: Todo, action: () -> Unit) {
		databaseUtil.insertTodo(todo, action)
	}
	
	override fun insertTodo(todo: List<Todo>, action: () -> Unit) {
		databaseUtil.insertTodo(todo, action)
	}
	
	override fun getAllCategory(action: (List<Category>) -> Unit) {
		databaseUtil.getAllCategory(action)
	}
	
	override fun getCategory(id: Int, action: (Category) -> Unit) {
		databaseUtil.getCategory(id, action)
	}
	
	override fun categorySize(action: (Int) -> Unit) {
		databaseUtil.categorySize(action)
	}
	
	override fun updateCategory(category: Category, action: () -> Unit) {
		databaseUtil.updateCategory(category, action)
	}
	
	override fun updateCategory(category: List<Category>, action: () -> Unit) {
		databaseUtil.updateCategory(category, action)
	}
	
	override fun deleteCategory(category: Category, action: () -> Unit) {
		databaseUtil.deleteCategory(category, action)
	}
	
	override fun deleteCategory(category: List<Category>, action: () -> Unit) {
		databaseUtil.deleteCategory(category, action)
	}
	
	override fun insertCategory(category: Category, action: () -> Unit) {
		databaseUtil.insertCategory(category, action)
	}
	
	override fun insertCategory(category: List<Category>, action: () -> Unit) {
		databaseUtil.insertCategory(category, action)
	}
	
	
	
	
	
	class FakeAppRepository: AppRepositoryI {
		override fun getAllTodo(action: (List<Todo>) -> Unit) {}
		
		override fun getTodoByCategoryID(categoryID: Int, action: (List<Todo>) -> Unit) {}
		
		override fun todoSize(action: (Int) -> Unit) {}
		
		override fun updateTodo(todo: Todo, action: () -> Unit) {}
		
		override fun updateTodo(todo: List<Todo>, action: () -> Unit) {}
		
		override fun deleteTodoByCategoryID(categoryID: Int, action: () -> Unit) {}
		
		override fun deleteTodo(todo: Todo, action: () -> Unit) {}
		
		override fun deleteTodo(todo: List<Todo>, action: () -> Unit) {}
		
		override fun insertTodo(todo: Todo, action: () -> Unit) {}
		
		override fun insertTodo(todo: List<Todo>, action: () -> Unit) {}
		
		override fun getAllCategory(action: (List<Category>) -> Unit) {}
		
		override fun getCategory(id: Int, action: (Category) -> Unit) {}
		
		override fun categorySize(action: (Int) -> Unit) {}
		
		override fun updateCategory(category: Category, action: () -> Unit) {}
		
		override fun updateCategory(category: List<Category>, action: () -> Unit) {}
		
		override fun deleteCategory(category: Category, action: () -> Unit) {}
		
		override fun deleteCategory(category: List<Category>, action: () -> Unit) {}
		
		override fun insertCategory(category: Category, action: () -> Unit) {}
		
		override fun insertCategory(category: List<Category>, action: () -> Unit) {}
		
	}
}