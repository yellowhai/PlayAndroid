/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hh.common.network

import kotlin.jvm.JvmOverloads
import kotlin.jvm.Volatile
import kotlin.Throws
import okhttp3.logging.HttpLoggingInterceptor
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor.Logger.Companion.DEFAULT
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.lang.NullPointerException
import java.lang.StringBuilder

class LogInterceptor @JvmOverloads constructor(private val logger: HttpLoggingInterceptor.Logger = DEFAULT) : Interceptor {

    @Volatile
    private var level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE

    /**
     * Change the level at which this interceptor logs.
     */
    fun setLevel(level: HttpLoggingInterceptor.Level?) {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
    }

    fun getLevel(): HttpLoggingInterceptor.Level {
        return level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level: HttpLoggingInterceptor.Level = findLevel(chain.request())
        if (level == HttpLoggingInterceptor.Level.NONE) return chain.proceed(chain.request())
        val builder = StringBuilder()
        val httpInterceptor = HttpLoggingInterceptor { message -> append(builder, message) }
        //可以单独为某个请求设置日志的级别，避免全局设置的局限性
        httpInterceptor.setLevel(level)
        val response = httpInterceptor.intercept(chain)
        logger.log(builder.toString())
        return response
    }

    private fun findLevel(request: Request): HttpLoggingInterceptor.Level {
        //可以单独为某个请求设置日志的级别，避免全局设置的局限性
        val logLevel = request.header(LOG_LEVEL)
        if (logLevel != null) {
            if (logLevel.equals("NONE", ignoreCase = true)) {
                return HttpLoggingInterceptor.Level.NONE
            } else if (logLevel.equals("BASIC", ignoreCase = true)) {
                return HttpLoggingInterceptor.Level.BASIC
            } else if (logLevel.equals("HEADERS", ignoreCase = true)) {
                return HttpLoggingInterceptor.Level.HEADERS
            } else if (logLevel.equals("BODY", ignoreCase = true)) {
                return HttpLoggingInterceptor.Level.BODY
            }
        }
        return level
    }

    companion object {
        private const val JSON_INDENT = 2
        private const val LOG_LEVEL = "LogLevel"
        private fun append(builder: StringBuilder, s: String) {
            var message = s
            if (TextUtils.isEmpty(message)) {
                return
            }
            try {
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if (message.startsWith("{") && message.endsWith("}")) {
                    val jsonObject = JSONObject(message)
                    message = jsonObject.toString(JSON_INDENT)
                } else if (message.startsWith("[") && message.endsWith("]")) {
                    val jsonArray = JSONArray(message)
                    message = jsonArray.toString(JSON_INDENT)
                }
            } catch (ignored: JSONException) {
            }
            builder.append(message).append('\n')
        }
    }

}