package com.xstore.tms.android.entity

import com.alibaba.fastjson.parser.DefaultJSONParser
import com.xstore.tms.android.utils.PreferencesUtils
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * 当前登录的配送员
 * Created by wangwenming1 on 2018/3/26.
 */
object LoginedCarrier {

    /**
     * 初始化用户信息
     */
    fun initLoginInfo(content: String?, carrierPin: String?, token: String?, sfKeyMD5: String?) {
        if (StringUtils.isNotBlank(content)) {
            var map: Map<String, Any>
                    = DefaultJSONParser(content).parseObject(HashMap<String, Any>(10)) as Map<String, Any>
            for (item in map) {
                when (item.key) {
                    "carrierName" -> this.carrierName = item.value as String
                    "carrierPhone" -> this.carrierPhone = item.value as String
                    "deptType" -> this.deptType = item.value as String
                    "storeName" -> this.storeName = item.value as String
                    "storeId" -> this.storeId = item.value as Int
                }
            }
        }
        if (StringUtils.isNotBlank(carrierPin)) {
            this.carrierPin = carrierPin
        }
        if (StringUtils.isNotBlank(token)) {
            this.token = token
        }
        if (StringUtils.isNotBlank(sfKeyMD5)) {
            this.sfKeyMD5 = sfKeyMD5
        }
    }

    /**
     * 清理登录状态
     */
    fun clearLoginStatus() {
        token = null
    }

    /**
     * 是否已经登录
     */
    fun isLogined() = token!!.isNotBlank()

    /**
     * 配送员 用户pin
     */
    var carrierPin: String? = null
        set(v) {
            field = v
            PreferencesUtils.safePut("l_c_carrierPin", v)
        }
        get() {
            if (field == null)
                field = PreferencesUtils.safeGet("l_c_carrierPin", "")
            return field
        }
    /**
     * 登入 token
     */
    var token: String? = null
        set(v) {
            field = v
            PreferencesUtils.safePut("l_c_token", v)
        }
        get() {
            if (field == null || field == ""){
                for(i in 0..3){
                    val result:String= PreferencesUtils.safeGet("l_c_token", "")
                    if(result.isBlank()){
                        continue
                    }else if(result.length>32){
                        PreferencesUtils.safePut("l_c_token", result.substring(0,32))
                        continue
                    }else{
                        field = result
                        break
                    }

                }
            }
            return field
        }


    /**
     * 配送员姓名
     */
    var carrierName: String? = null
        private set(v) {
            field = v
            PreferencesUtils.safePut("l_c_carrierName", v)
        }
        get() {
            if (field == null)
                field = PreferencesUtils.safeGet("l_c_carrierName", "")
            return field
        }


    /**
     * 配送员电话
     */
    var carrierPhone: String? = null
        private set(v) {
            field = v
            PreferencesUtils.safePut("l_c_carrierPhone", v)
        }
        get() {
            if (field == null)
                field = PreferencesUtils.safeGet("l_c_carrierPhone", "")
            return field
        }

    /**
     * 部门类型
     */
    var deptType: String? = null
        private set(v) {
            field = v
            PreferencesUtils.safePut("l_c_deptType", v)
        }
        get() {
            if (field == null)
                field = PreferencesUtils.safeGet("l_c_deptType", "")
            return field
        }

    /**
     * 门店号
     */
    var storeId: Int? = null
        private set(v) {
            field = v
            PreferencesUtils.safePut("l_c_storeId",  v?.toString())
        }
        get() {
            if (field == null)
                field =  PreferencesUtils.safeGet("l_c_storeId", "0")?.toIntOrNull()
            return field
        }

    /**
     * 门店名称
     */
    var storeName: String? = null
        private set(v) {
            field = v
            PreferencesUtils.safePut("l_c_storeName", v)
        }
        get() {
            if (field == null)
                field = PreferencesUtils.safeGet("l_c_storeName", "")
            return field
        }

    /**
     * 安全（啸约定无意义）
     */
    val sfTempStr = "success"

    /**
     * 安全，登入后返回的MD5 KEY;
     */
    var sfKeyMD5: String? = null
        private set(v) {
            field = v
            PreferencesUtils.safePut("l_c_sfKeyMD5", v)
        }
        get() {
            if (field == null || field == ""){
                for(i in 0..3){
                    val result:String= PreferencesUtils.safeGet("l_c_sfKeyMD5", "")
                    if(result.isBlank()){
                        continue
                    }else if(result.length>32){
                        PreferencesUtils.safePut("l_c_sfKeyMD5", result.substring(0,32))
                        continue
                    }else{
                        field = result
                        break
                    }
                }
            }
            return field
        }
}