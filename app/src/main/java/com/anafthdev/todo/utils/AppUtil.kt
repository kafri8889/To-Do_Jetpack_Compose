package com.anafthdev.todo.utils

import android.content.Context
import android.widget.Toast

object AppUtil {
	
	/**
	 * example: arr = [1, 2, 3, 4 ,5]
	 *        : arr.getWithSize(2),
	 *  result: [1, 2]
	 */
	fun <T> List<T>.getWithSize(size: Int): List<T> {
		if (this.size <= size) return this
		
		val list = ArrayList<T>()
		for (i in 0..size) { list.add(this[i]) }
		
		return list
	}
	
	fun Any.toast(context: Context, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(context, this.toString(), length).show()
}