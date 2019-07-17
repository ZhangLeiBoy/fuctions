package com.android.albert.log

import android.util.Log

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief 日志打印服务
 */
object LogUtils {

    private val TAG = "appplant"

    private fun log(tag: String, msg: String, level: Int) {
        var msgs = msg
        var tags = tag
        val e = Exception()
        val els = e.stackTrace
        val logDetails: String
        var i = 0
        val l = els.size
        while (i < l) {
            var className :String = els[i].className
            if (className != LogUtils::class.qualifiedName) {
                logDetails = (els[i].fileName + "->"
                        + els[i].methodName + ":"
                        + els[i].lineNumber + " ")
                msgs = logDetails + msgs
                break
            }
            i++
        }
        when (level) {
            Log.DEBUG -> Log.d(tags, msgs + "")
            Log.INFO -> Log.i(tags, msgs + "")
            Log.WARN -> Log.w(tags, msgs + "")
            Log.ERROR -> Log.e(tags, msgs + "")
            else -> Log.d(tag, msgs + "")
        }
    }

    /**
     * Simple log
     *
     * @param tag
     * @param msg
     */
    fun log(tag: String, msg: String) {
        log(tag, msg + "", Log.DEBUG)
    }

    fun log(msg: String) {
        log(TAG, msg + "")
    }

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        log(tag, msg + "", Log.INFO)
    }

    fun e(msg: String) {
        e(TAG, msg + "")
    }

    fun e(tag: String, msg: String) {
        log(tag, msg + "", Log.ERROR)
    }

    fun d(msg: String) {
        d(TAG, msg + "")
    }

    fun d(tag: String, msg: String) {
        log(tag, msg + "", Log.DEBUG)
    }
}