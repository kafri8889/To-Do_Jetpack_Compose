package com.anafthdev.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "todo_table")
data class Todo(
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "content") val content: String,
	@ColumnInfo(name = "date") val date: Long,
	@ColumnInfo(name = "dateCreated") val dateCreated: Long,
	@ColumnInfo(name = "categoryID") val categoryID: Int,
	@ColumnInfo(name = "checkboxes") val checkboxes: List<TodoCheckbox>,
	@ColumnInfo(name = "isComplete") var isComplete: Boolean = false,
	@PrimaryKey val id: Int = Random.nextInt()
) {
	companion object {
		val todo_sample = Todo(
			title = "Any Todo",
			content = "Any Content",
			date = System.currentTimeMillis(),
			dateCreated = System.currentTimeMillis(),
			categoryID = Category.default.id,
			checkboxes = emptyList(),
		)
	}
}
