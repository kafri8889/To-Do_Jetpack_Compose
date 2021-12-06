package com.anafthdev.todo.model

import kotlin.random.Random

data class TodoCheckbox(
	var title: String,
	var isComplete: Boolean = false,
	var id: Int = Random.nextInt()
)
