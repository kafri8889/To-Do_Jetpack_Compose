package com.anafthdev.todo.database

import androidx.room.TypeConverter
import com.anafthdev.todo.model.TodoCheckbox
import com.google.gson.Gson

object DatabaseTypeConverter {
	
	@TypeConverter
	fun todoCheckboxesToJson(checkboxes: List<TodoCheckbox>) = Gson().toJson(checkboxes)!!
	
	@TypeConverter
	fun jsonToTodoCheckboxes(jsonTodoCheckbox: String) = Gson().fromJson(jsonTodoCheckbox, Array<TodoCheckbox>::class.java).toList()
}