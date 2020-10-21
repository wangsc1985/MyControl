package com.wang17.mycontrol.util

import android.os.Environment
import java.io.File

/**
 * Created by 阿弥陀佛 on 2016/10/28.
 */
object _Session {
    @JvmField
    val ROOT_DIR = File(Environment.getExternalStorageDirectory().toString() + "/0/mycontorl")

    init {
        try {
            /**
             * 加载念佛音乐列表
             */
            if (!ROOT_DIR.exists()) {
                ROOT_DIR.mkdirs()
            }
            /**
             * unit*cose：一个点位多少钱/手。
             */
        } catch (e: Exception) {
            throw e
        }
    }
}