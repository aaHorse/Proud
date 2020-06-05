package com.horse.proud.data.network

import com.horse.core.proud.model.regist.Register
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.Before

/**
 * 注册、登录接口测试用例
 *
 * @author liliyuan
 * @since 2020年5月7日14:02:14
 * */
class LoginNetworkTest {

    lateinit var network:LoginNetwork

    @Before
    fun setUp(){
        network = LoginNetwork()
    }

    @Test
    fun fetchRegister() {
        val register = Register()
        register.name = name
        register.number = number
        register.password = password
        launch({
            val response = network.fetchRegister(register)
            assertEquals(200,response.status)
        },{

        })
    }

    @Test
    fun fetchLogin() {
        launch({
            val login = network.fetchLogin(number, password)
            assertEquals(200,login.status)
        },{

        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = GlobalScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }

    companion object{

        const val name:String = "会飞的鱼"

        const val number:String = "221701489"

        const val password = "mimimimimi"

    }
}