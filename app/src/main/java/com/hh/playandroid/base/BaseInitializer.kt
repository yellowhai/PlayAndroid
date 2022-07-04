package com.hh.playandroid.base

import android.content.Context
import androidx.startup.Initializer

class BaseInitializer : Initializer<Unit> {
    override fun create(context: Context) {

    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}