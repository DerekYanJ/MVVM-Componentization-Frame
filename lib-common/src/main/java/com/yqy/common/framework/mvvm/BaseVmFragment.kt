package com.yqy.common.framework.mvvm

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orhanobut.logger.Logger
import com.yqy.common.base.fragment.BaseFragment
import com.yqy.net.LoadState

/**
 * @desc
 * @author derekyan
 * @date 2020/5/15
 */
abstract class BaseVmFragment<VM : BaseViewModel> : BaseFragment() {
    lateinit var mViewModel: VM

    abstract fun getViewModel(): VM?

    fun getViewModelKey() = "$this"

    override fun initViewBefore() {
        super.initViewBefore()

        getViewModel()?.let { it ->
            mViewModel =
                ViewModelProvider(requireActivity()).get(getViewModelKey(), it::class.java)
            mViewModel.getLoadState()?.observe(this, Observer {
                when (it) {
                    is LoadState.Success -> {
                        Logger.e("mViewModel LoadState = Success")
                        hideProgress()
                    }
                    is LoadState.Loading -> {
                        Logger.e("mViewModel LoadState = Loading")
                        showProgress()
                    }
                    is LoadState.FailApi -> {
                        Logger.e("mViewModel LoadState = Fail  ${it.msg}  + ${it.apiException}")
                        hideProgress()
                        showError(it.msg)
                    }
                }
            })
        }
    }

}