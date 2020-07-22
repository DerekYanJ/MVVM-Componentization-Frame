package com.yqy.common.framework.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yqy.common.coroutine.CoroutineHelper
import com.yqy.net.AbsResult
import com.yqy.net.LoadState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @desc
 * @author derekyan
 * @date 2020/5/15
 */
abstract class BaseViewModel: ViewModel() {

    private var mCoroutineHelper: CoroutineHelper? = CoroutineHelper(viewModelScope)

    /**
     * ViewModel扩展方法：启动协程
     * @param block 协程逻辑
     * @param onError 错误回调方法
     * @param onComplete 完成回调方法
     */
    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        onError: (e: Throwable) -> Unit = {},
        onComplete: () -> Unit = {}
    ) {
        mCoroutineHelper?.launch(block, onError, onComplete)
    }

    fun getLoadState(): MutableLiveData<LoadState>? = mCoroutineHelper?.getLoadState()

    /**
     * io 线程请求
     */
    suspend fun <T : Any> request(call: suspend () -> AbsResult<T>): AbsResult<T> {
        return withContext(Dispatchers.IO){
            call.invoke()
        }
    }
    suspend fun <T : Any> requestT(call: suspend () -> T): T {
        return withContext(Dispatchers.IO){
            call.invoke()
        }
    }

    /**
     * ui 线程请求
     */
    suspend fun <T : Any> requestUi(call: suspend () -> AbsResult<T>): AbsResult<T> {
        return withContext(Dispatchers.Main){ call.invoke()}
    }
    suspend fun <T : Any> requestUiT(call: suspend () -> T): T {
        return withContext(Dispatchers.Main){ call.invoke()}
    }


    override fun onCleared() {
        super.onCleared()
        mCoroutineHelper = null
    }

}