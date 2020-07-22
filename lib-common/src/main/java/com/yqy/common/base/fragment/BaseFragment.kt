package com.yqy.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.gyf.immersionbar.ImmersionBar
import com.yqy.common.ToastUtil
import com.yqy.common.base.IBaseView
import com.yqy.common.event.EmptyEvent
import com.yqy.net.error.ApiException
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
abstract class BaseFragment: AbsFragment(), View.OnClickListener,
    IBaseView {

    private lateinit var rootView: View
    private var isInit = false // 是否已经初始化了

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParameters()
    }
 
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBar()
        initNavigationBar()
        initViewBefore()

        EventBus.getDefault().register(this)

        initView(savedInstanceState)
        isInit = true
        initData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEmptyEvent(event: EmptyEvent) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun isShowToolbar(): Boolean = false

    override fun onClick(v: View?) {}

    override fun <V : View> findViewById(@IdRes viewId: Int): V {
        if (!isInit)
            throw RuntimeException("Fragment not initialized")
        return rootView.findViewById(viewId)
    }

    override fun initParameters() {}

    override fun initNavigationBar() {
    }

    override fun initViewBefore() {}

    /**
     * 默认暗色状态栏
     */
    override fun isDarkStatusBar(): Boolean = true

    override fun initStatusBar() {
        if (isDarkStatusBar()) {
            setDarkStatusBar()
        } else {
            setLightStatusBar()
        }
    }

    /**
     * 状态栏 暗色
     */
    fun setDarkStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
    }

    /**
     * 状态栏 亮色
     */
    fun setLightStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(false).init()
    }



    override fun showProgress() {
//        Logger.e("showProgress()")
    }

    override fun hideProgress() {
//        Logger.e("hideProgress()")
    }

    override fun showError(error: String) {
        showToast(error)
    }

    override fun showToast(msg: String) {
        if (this != null && this.isVisible)
            ToastUtil.show(requireContext(), msg)
    }

    override fun showError(apiException: ApiException) {

    }
}