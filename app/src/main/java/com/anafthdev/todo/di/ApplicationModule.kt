package com.anafthdev.todo.di

import com.anafthdev.todo.TodoApplication
import com.anafthdev.todo.common.Repository
import com.anafthdev.todo.database.ApplicationDatabase
import com.anafthdev.todo.utils.DatabaseUtil
import com.anafthdev.todo.common.TodoViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: TodoApplication) {
	
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
	fun provideAppRepository() = Repository(provideDatabaseUtil())
	
	@Singleton
	@Provides
	fun provideAppViewModel() = TodoViewModel(provideAppRepository())
}