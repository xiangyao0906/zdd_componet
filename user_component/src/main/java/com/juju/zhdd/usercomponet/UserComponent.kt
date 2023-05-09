package com.juju.zhdd.usercomponet

import com.billy.cc.core.component.CC
import com.billy.cc.core.component.CCResult
import com.billy.cc.core.component.CCUtil
import com.billy.cc.core.component.IComponent
import com.juju.zhdd.usercomponet.login.LoginActivity
/**
 *
 * <p>向外暴露的组件声明
 * Created by xiangyao on 2023/5/9.
 */
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