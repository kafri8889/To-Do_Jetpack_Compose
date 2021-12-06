package com.anafthdev.todo

import android.app.Application
import com.anafthdev.todo.di.ApplicationComponent
import com.anafthdev.todo.di.ApplicationModule
import com.anafthdev.todo.di.DaggerApplicationComponent

class TodoApplication: Application() {
	
	val appComponent: ApplicationComponent = DaggerApplicationComponent.builder()
		.applicationModule(ApplicationModule(this))
		.build()
}