package com.anafthdev.todo.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerMenu(
	val name: String,
	val iconVector: ImageVector?,
	val iconRes: Painter?,
)
