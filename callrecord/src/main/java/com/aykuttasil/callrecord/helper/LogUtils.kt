package com.aykuttasil.callrecord.helper

import timber.log.Timber

object LogUtils {

    @JvmStatic
    fun i(tag: String, message: String?) {
        Timber.i("""$tag ${message ?: "log message is null"}""")
    }

    @JvmStatic
    fun i(message: String?) {
        Timber.i(message ?: "log message is null")
    }

    @JvmStatic
    fun d(message: String = "") {
        Timber.d(message)
    }

    @JvmStatic
    fun d(tag: String, message: String = "") {
        Timber.d("""$tag $message""")
    }

    @JvmStatic
    fun e(throwable: Throwable, message: String = "") {
        Timber.e(throwable)
    }

    @JvmStatic
    fun e(throwable: Throwable, tag: String, message: String?) {
        Timber.e(throwable, """$tag ${message ?: "log message is null"}""")
    }

    @JvmStatic
    fun info(message: String = "") {
        Timber.i(message)
    }

    @JvmStatic
    fun error(throwable: Throwable, message: String = "") {
        Timber.e(throwable)
    }

    @JvmStatic
    fun error(message: String = "") {
        Timber.e(message)
    }
}