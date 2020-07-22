package com.yqy.common.coroutine

import androidx.lifecycle.MutableLiveData
import com.yqy.common.manager.DialogManager
import com.yqy.net.AbsResult
import com.yqy.net.error.ApiException
import com.yqy.net.LoadState
import com.yqy.net.ExceptionHandle
import kotlinx.coroutines.*

/**
 * @desc
 * @author derekyan
 * @date 2020/7/17
 */
class CoroutineHelper {
    private val job = SupervisorJob()
    var uiScope: CoroutineScope

    var loadStateLiveData: MutableLiveData<LoadState>

    companion object {
        fun build() {

        }
    }

    constructor() {
        loadStateLiveData = MutableLiveData()
        uiScope = CoroutineScope(Dispatchers.Main + job)
    }

    constructor(scope: CoroutineScope) {
        loadStateLiveData = MutableLiveData()
        uiScope = scope
    }

    constructor(loadStateLiveData:  MutableLiveData<LoadState>, scope: CoroutineScope) {
        this.loadStateLiveData = loadStateLiveData
        this.uiScope = scope
    }

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
        // 加载中
        loadStateLiveData.postValue(LoadState.Loading())

        // 打开加载弹窗
        DialogManager.showLoadingDialog()

        uiScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                run {
                    DialogManager.dismissLoadingDialog()
                    // 这里统一处理错误
                    // 加载失败
                    if (throwable is ApiException)
                        loadStateLiveData.postValue(LoadState.FailApi(throwable))
                    else
                        loadStateLiveData.postValue(LoadState.FailApi(ExceptionHandle.handleException(throwable)))

                    throwable.printStackTrace()
                    onError(throwable)
                }
            }
        ) {
            try {
                block.invoke(this)
            } finally {
                // 加载成功
                loadStateLiveData.postValue(LoadState.Success())
                // 关闭加载弹窗
                DialogManager.dismissLoadingDialog()
                onComplete()
            }
        }
    }

    fun getLoadState(): MutableLiveData<LoadState> = loadStateLiveData

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

    fun onDestroy () {
        job.cancel()
    }
}