package com.hh.playandroid.util

import com.hh.playandroid.logic.HttpDataHelper
import com.hh.playandroid.logic.Repository
import com.hh.playandroid.logic.SqliteDataHelper

val repository by lazy{ ServiceLocator.provideRepository()}

object ServiceLocator {
    /**
     * Provide the Repository instance that ViewModel should depend on.
     */
    fun provideRepository() = Repository(provideDataHelper(), provideSqliteDataHelper())

    /**
     * Provide the DataHelper instance that Repository should depend on.
     */
    private fun provideDataHelper() = HttpDataHelper()

    /**
     * Provide the SqliteDataHelper instance that Repository should depend on.
     */
    private fun provideSqliteDataHelper() = SqliteDataHelper()
}