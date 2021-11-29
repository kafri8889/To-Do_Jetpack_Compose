package com.anafthdev.todo.di

import android.app.Application

class Application: Application() {
	
	val appComponent: ApplicationComponent = DaggerApplicationComponent
		.builder().applicationModule(ApplicationModule(this))
		.build()
}