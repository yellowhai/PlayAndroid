package com.hh.common.util

import android.content.Context
import android.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.hh.common.base.YshhApplication.Companion.applicationScope
import com.hh.common.base.YshhApplication.Companion.context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/4  15:35
 */
val SP_CONFIG by lazy{"setting"}

val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = SP_CONFIG)

object CacheUtils {
    private val ds by lazy {
        val application = context
        application.dataStore
    }

    private val cache = mutablePreferencesOf()

    //runBlocking 是会阻塞主线程的，直到 runBlocking 内部全部子任务执行完毕，才会继续执行下一步的操作！
    fun <T> safeKeyDelegate(key: Preferences.Key<T>, defaultValue: T) =
        object : ReadWriteProperty<Any?, T> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                cache[key] ?: runBlocking { ds.data.map { it[key] ?: defaultValue }.first() }

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                cache[key] = value
                applicationScope.launch { ds.edit { it[key] = value } }
            }
        }

    var themeColor: Int by safeKeyDelegate(intPreferencesKey("color"), Color.parseColor("#004D40"))

    var avatar: String by safeKeyDelegate(stringPreferencesKey("avatar"), "")

    var isLogin: Boolean by safeKeyDelegate(booleanPreferencesKey("isLogin"),false)

    var userInfo: String by safeKeyDelegate(stringPreferencesKey("userInfo"),"")

    var isNight: Boolean by safeKeyDelegate(booleanPreferencesKey("isNight"),false)
}