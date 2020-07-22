package com.yqy.common.base.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.gyf.immersionbar.ImmersionBar
import com.zs.common.R
import com.yqy.common.ToastUtil
import com.yqy.common.base.IBaseView
import com.yqy.common.event.EmptyEvent
import com.yqy.common.manager.SpManager
import com.yqy.net.error.ApiException
import com.yqy.common.ui.toolbar.ToolBarHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
abstract class BaseActivity: AbsActivity(),
    IBaseView {

    lateinit var mToolbar: Toolbar
    val spUtils = SpManager.getSp()

    override fun onCreate(savedInstanceState: Bundle?) {
        superOnCreateBefore()
        super.onCreate(savedInstanceState)
        setContentViewBefore()

        val contentViewId = getLayoutId()
        if (contentViewId <= 0)
            throw RuntimeException("You must set the layout ID")

        if (isShowToolbar()) {
            setContentViewWithToolBar(contentViewId)
        } else {
            setContentView(contentViewId)
        }

        EventBus.getDefault().register(this)

        notAllowScreenShot()
        initViewBefore()
        initStatusBar()
        initNavigationBar()
        initParameters()
        initView(savedInstanceState)
        initData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEmptyEvent(event: EmptyEvent) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 默认不设置Toolbar
     */
    override fun isShowToolbar(): Boolean = true

    override fun superOnCreateBefore() {}

    override fun setContentViewBefore() {}

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
    private fun setDarkStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
    }

    /**
     * 状态栏 亮色
     */
    private fun setLightStatusBar() {
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
        ToastUtil.show(this, msg)
    }

    override fun showError(apiException: ApiException) {

    }

    private fun notAllowScreenShot(){
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun setContentViewWithToolBar(layoutResID: Int) {
        val mToolBarHelper = ToolBarHelper(
            this,
            layoutResID,
            R.drawable.ic_back_black,
            R.color.white
        )
        mToolbar = mToolBarHelper.toolBar
        setContentView(mToolBarHelper.contentView) /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(mToolbar)
        onCreateCustomToolBar(mToolbar)
        mToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    protected fun setNavTitle(title: String) {
        val actionBar = supportActionBar
        actionBar?.title = title
    }

    /**
     * 设置标题
     * @param title
     */
    protected fun setToolBarCenterTitle(title: String) {
        val actionBar = supportActionBar
        actionBar?.title = ""
        val view: View? = mToolbar.findViewById(R.id.tv_toolbar_title)
        (view as TextView).text = title
    }

    protected fun setToolBarLeftTitle(title: String) {
        val actionBar = supportActionBar
        actionBar?.title = ""
        val view: View? = mToolbar.findViewById(R.id.tv_left_title)
        (view as TextView).text = title
    }

    /**
     * 设置右侧文字
     * @param str
     */
    protected fun setToolBarRightText(str: String,listener:View.OnClickListener) {
        val view = mToolbar.rootView.findViewById<View>(R.id.tv_toolbar_right)
        view.visibility = View.VISIBLE
        view.setOnClickListener(listener)
        (view as TextView).text = str
    }

    /**
     * 设置右侧文字
     * @param str
     */
    protected fun setToolBarRightText(str: String) {
        val view = mToolbar.rootView.findViewById<View>(R.id.tv_toolbar_right)
        view.visibility = View.VISIBLE
        (view as TextView).text = str
    }

    protected fun getToolBarRightView(): TextView {
        return mToolbar.findViewById(R.id.tv_toolbar_right)
    }

    private fun onCreateCustomToolBar(toolbar: Toolbar) {
        toolbar.setContentInsetsRelative(0, 0)
    }



    /**
     * 获取uri
     */
    fun getFileUri(path: String, intent: Intent?): Uri {
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                this,
                "com.zs.live.FileProvider",
                File(path)
            )
            intent?.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(File(path))
        }
        return uri
    }

}