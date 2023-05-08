package com.juju.zhdd.user_componet

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.CCUtil
import com.billy.cc.core.component.IComponent

class UserComponent : IComponent {
    override fun getName(): String {
        return "user_component"
    }

    override fun onCall(cc: CC?): Boolean {

        when (cc?.actionName ?: "") {
            "showActivity" -> {
                CCUtil.navigateTo(cc, LoginActivity::class.java)
                CC.sendCCResult(cc?.callId, CCResult.success())
            }
            else -> {

            }
        }

        return false
    }
}