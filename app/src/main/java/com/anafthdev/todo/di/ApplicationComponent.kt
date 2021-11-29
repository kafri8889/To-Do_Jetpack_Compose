package com.anafthdev.todo.di

import com.anafthdev.todo.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
	modules = [
		ApplicationModule::class
	]
)
interface ApplicationComponent {
	
	fun inject(context: MainActivity)
}