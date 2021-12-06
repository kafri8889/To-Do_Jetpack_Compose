package com.anafthdev.todo.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object AppUtil {
	
	/**
	 * example: arr = [1, 2, 3, 4 ,5]
	 *        : arr.getWithSize(2),
	 *  result: [1, 2]
	 *  @author kafri8889
	 */
	fun <T> List<T>.getWithSize(size: Int): List<T> {
		if (this.size <= size) return this
		
		val list = ArrayList<T>()
		for (i in 0..size) { list.add(this[i]) }
		
		return list
	}
	
	/**
	 * Returns an item from a list that matches the given predicate.
	 * null if there is no matching item
	 * @author kafri8889
	 */
	fun <T> Collection<T>.get(predicate: (T) -> Boolean): T? {
		var result: T? = null
		for (item in this) {
			if (predicate(item)) {
				result = item
				break
			}
		}
		
		return result
	}
	
	fun Any.toast(context: Context, length: Int = Toast.LENGTH_SHORT) = Handler(Looper.getMainLooper()).post {
		Toast.makeText(context, this.toString(), length).show()
	}
}