package com.anafthdev.todo.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// MDC 3
//@SuppressLint("ConflictingOnColor")
//private val LightColorScheme = lightColorScheme(
//	primary = Purple500,
//	onPrimary = on_primary_light,
//	primaryContainer = primary_container_light,
//	onPrimaryContainer = on_primary_container_light,
//	inversePrimary = Color.Green,
//
//	secondary = secondary_light,
//	onSecondary = on_secondary_light,
//	secondaryContainer = secondary_container_light,
//	onSecondaryContainer = on_secondary_container_light,
//
//	tertiary = tertiary_light,
//	onTertiary = on_tertiary_light,
//	tertiaryContainer = tertiary_container_light,
//	onTertiaryContainer = on_tertiary_container_light,
//
//	surface = surface_light,
//	onSurface = on_surface_light,
//	surfaceVariant = surface_variant_light,
//	onSurfaceVariant = on_surface_variant_light,
//	inverseSurface = Color.Green,
//	inverseOnSurface = Color.Green,
//
//	error = error_light,
//	onError = on_error_light,
//	errorContainer = error_container_light,
//	onErrorContainer = on_error_container_light,
//
//	background = background_light,
//	onBackground = on_background_light,
//
//	outline = outline,
//)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
	
	primary = primary_light,
	onPrimary = on_primary_light,
	primaryVariant = primary_container_light,
	
	secondary = secondary_light,
	onSecondary = on_secondary_light,
	secondaryVariant = secondary_container_light,
	
	background = background_light,
	onBackground = on_background_light,
	
	surface = surface_light,
	onSurface = on_surface_light,
	
	error = error_light,
	onError = on_error_light,
)

@Composable
fun TODOTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//	val colors = LightColorScheme
	val colors = LightColorPalette
	
//	MaterialTheme(
//		typography = Typography,
//		content = content,
//		colorScheme = colors
//	)
	
	MaterialTheme(
		typography = Typography,
		colors = colors
	) {
		content()
	}
}