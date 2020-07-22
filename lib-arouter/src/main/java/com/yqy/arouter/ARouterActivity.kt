package com.yqy.arouter

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.zs.arouter.R
import com.yqy.common.base.activity.BaseActivity
import com.yqy.common.third.arouter.PathConstant

@Route(path = PathConstant.ARouter.ARouter1)
class ARouterActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_arouter

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }
}
