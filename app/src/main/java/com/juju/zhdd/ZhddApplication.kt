package com.juju.zhdd

import android.app.Application
import com.billy.cc.core.component.CC

class ZhddApplication:Application() {


    override fun onCreate() {
        super.onCreate()
        CC.enableVerboseLog(true)
        CC.enableDebug(true)
        CC.enableRemoteCC(true)
    }
}