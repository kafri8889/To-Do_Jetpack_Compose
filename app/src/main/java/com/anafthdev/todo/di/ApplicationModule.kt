package com.anafthdev.todo.di

import com.anafthdev.todo.database.ApplicationDatabase
import com.anafthdev.todo.utils.DatabaseUtil
import com.anafthdev.todo.view_model.AppViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
	
	@Singleton
	@Provides
	fun provideApplication() = application
	
	@Singleton
	@Provides
	fun provideApplicationDatabase() = ApplicationDatabase.getInstance(application)
	
	@Singleton
	@Provides
	fun provideDatabaseUtil() = DatabaseUtil(provideApplicationDatabase())
	
	@Singleton
	@Provides
	fun provideAppViewModel() = AppViewModel(provideDatabaseUtil())
}