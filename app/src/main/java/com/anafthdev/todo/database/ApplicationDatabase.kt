package com.anafthdev.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.Todo

@Database(entities = [
	Todo::class,
	Category::class
], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverter::class)
abstract class ApplicationDatabase: RoomDatabase() {
	
	abstract fun databaseDAO(): ApplicationDatabaseDAO
	
	companion object {
		
		private var INSTANCE: ApplicationDatabase? = null
		
		fun getInstance(base: Context): ApplicationDatabase {
			if (INSTANCE == null) {
				synchronized(ApplicationDatabase::class.java) {
					INSTANCE = Room.databaseBuilder(base, ApplicationDatabase::class.java, "app.db")
						.build()
				}
			}
			
			return INSTANCE!!
		}
	}
}