package com.anafthdev.todo.utils

import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import android.graphics.Rect
import androidx.compose.ui.platform.LocalView

object ComposeUtil {
	
	enum class Keyboard {
		Opened, Closed
	}
	
	@Composable
	fun keyboardAsState(): State<Keyboard> {
		val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
		val view = LocalView.current
		DisposableEffect(view) {
			val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
				val rect = Rect()
				view.getWindowVisibleDisplayFrame(rect)
				val screenHeight = view.rootView.height
				val keypadHeight = screenHeight - rect.bottom
				keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
					Keyboard.Opened
				} else {
					Keyboard.Closed
				}
			}
			view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
			
			onDispose {
				view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
			}
		}
		
		return keyboardState
	}
}