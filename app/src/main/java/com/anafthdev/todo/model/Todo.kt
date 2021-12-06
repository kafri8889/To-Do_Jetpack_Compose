package com.anafthdev.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "todo_table")
data class Todo(
	@ColumnInfo(name = "title") var title: String,
	@ColumnInfo(name = "content") var content: String,
	@ColumnInfo(name = "date") var date: Long,
	@ColumnInfo(name = "dateCreated") val dateCreated: Long,
	@ColumnInfo(name = "categoryID") var categoryID: Int,
	@ColumnInfo(name = "checkboxes") var checkboxes: List<TodoCheckbox>,
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
