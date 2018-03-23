package Lib.callback

import java.lang.reflect.ParameterizedType

/**
 * 创建时间：
 * 更新时间 2017/10/30 15:07
 * 版本：
 * 作者：Mr.jin
 * 描述：网络调用回调，支持泛型
 */

abstract class ObjectCallBack<T> {
    val clazz: Class<List<T>>

    init {
        val type = this.javaClass
                .genericSuperclass as ParameterizedType
        this.clazz = type.actualTypeArguments[0] as Class<List<T>>
    }

    abstract fun onSuccess(t: List<T>)

    abstract fun onError(error: Int, msg: String)

}
