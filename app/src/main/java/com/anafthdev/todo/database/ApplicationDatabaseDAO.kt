package com.anafthdev.todo.database

import androidx.room.*
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo

@Dao
interface ApplicationDatabaseDAO {
	
	@Query("SELECT * FROM todo_table")
	suspend fun getAllTodo(): List<Todo>
	
	@Query("SELECT * FROM todo_table WHERE categoryID LIKE :mCategoryID")
	suspend fun getTodoByCategoryID(mCategoryID: Int): List<Todo>
	
	@Query("SELECT count(id) FROM todo_table")
	suspend fun todoCount(): Int
	
	@Query("SELECT count(id) FROM todo_table WHERE categoryID LIKE :mCategoryID")
	suspend fun todoSizeByCategoryID(mCategoryID: Int): Int
	
	@Update
	suspend fun updateTodo(todo: Todo)
	
	@Update
	suspend fun updateTodo(todo: List<Todo>)
	
	@Delete
	suspend fun deleteTodo(todo: Todo)
	
	@Delete
	suspend fun deleteTodo(todo: List<Todo>)
	
	@Query("DELETE FROM todo_table")
	suspend fun deleteAllTodo()
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertTodo(todo: Todo)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertTodo(todo: List<Todo>)
	
	
	
	@Query("SELECT * FROM category_table")
	suspend fun getAllCategory(): List<Category>
	
	@Query("SELECT * FROM category_table WHERE id LIKE :mCategoryID")
	suspend fun getCategory(mCategoryID: Int): Category
	
	@Query("SELECT count(id) FROM category_table")
	suspend fun categorySize(): Int
	
	@Update
	suspend fun updateCategory(category: Category)
	
	@Update
	suspend fun updateCategory(category: List<Category>)
	
	@Delete
	suspend fun deleteCategory(category: Category)
	
	@Delete
	suspend fun deleteCategory(category: List<Category>)
	
	@Query("DELETE FROM category_table")
	suspend fun deleteAllCategory()
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertCategory(category: Category)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertCategory(category: List<Category>)
	
}