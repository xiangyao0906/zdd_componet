package com.juju.core.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object CoreRexUtils {


    //身份证
    const val IDCARD = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)\$"

    /** 检查护照是否合法  */
    const val PASSPORT1 = "/^[a-zA-Z]{5,17}$/"


    var phoneReg = "^(1[3-9])\\d{9}$"   //手机号正则格式

    var emailReg =
        "[\\w!#\$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#\$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?"    //邮箱正则格式
    var nameReg = "^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$"    //名字格式
    var nameReg2 = "^[\\u4e00-\\u9fa5]+$"    //名字格式2

    var chinesReg = "[\u0391-\uFFE5]+$"

    // 定义正则表达式
    var ipRegex = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)"

    fun checkIdCard(idCard: String): Boolean {
        return Pattern.matches(IDCARD, idCard)
    }

    fun isAllChinese(idCard: String): Boolean {
        return Pattern.matches(chinesReg, idCard)
    }

    fun checkPassPort(passPort: String): Boolean {
        return Pattern.matches(PASSPORT1, passPort)
    }

    fun checkPhone(phone: String): Boolean {
        return Pattern.matches(phoneReg, phone)
    }

    fun checkEmail(email: String): Boolean {
        return Pattern.matches(emailReg, email)
    }

    fun isPassword(password: String?): Boolean {
        val regex = "^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{6,20}\$"
        val p = Pattern.compile(regex)
        val m: Matcher = p.matcher(password)
        return m.matches()
    }

    /**
     * 验证输入的名字是否为“中文”或者是否包含“·”
     */
    fun isLegalName(name: String): Boolean {
        return if (name.contains("·") || name.contains("•")) {
            name.matches(Regex(nameReg))
        } else {
            name.matches(Regex(nameReg2))
        }
    }


    fun isLocalIP(ipContent: String?): Boolean {
        if (ipContent != null && ipContent.isNotEmpty()) {
            // 判断ip地址是否与正则表达式匹配
            return Pattern.matches(ipRegex, ipContent)
        }
        return false
    }

    fun isInnerIp(ipContent:String):Boolean{
        if(isLocalIP(ipContent)){
            val v4 = IPUtils.textToNumericFormatV4(ipContent) ?: return false
            return IPUtils.internalIp(v4)
        }else{
            return false
        }

    }



}