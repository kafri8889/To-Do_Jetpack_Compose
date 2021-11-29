package com.anafthdev.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "category_table")
data class Category(
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "color") var color: Long,
	@PrimaryKey val id: Int = Random.nextInt()
)
