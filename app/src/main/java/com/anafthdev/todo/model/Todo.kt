package com.anafthdev.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "todo_table")
data class Todo(
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "content") val content: String,
	@ColumnInfo(name = "date") val date: String,
	@ColumnInfo(name = "categoryID") val categoryID: Int,
	@ColumnInfo(name = "checkboxes") val checkboxes: List<TodoCheckbox>,
	@PrimaryKey val id: Int = Random.nextInt()
)
