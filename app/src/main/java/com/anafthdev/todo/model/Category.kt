package com.anafthdev.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anafthdev.todo.data.CategoryColor
import kotlin.random.Random

@Entity(tableName = "category_table")
data class Category(
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "color") var color: Long,
	@PrimaryKey val id: Int = Random.nextInt()
) {
	companion object {
		val default = Category(
			id = 1,
			name = "Any",
			color = CategoryColor.values[0]
		)
	}
}
