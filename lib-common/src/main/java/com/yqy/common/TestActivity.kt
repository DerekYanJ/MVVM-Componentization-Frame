package com.yqy.common

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.yqy.common.base.activity.BaseActivity

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
class TestActivity: BaseActivity() {

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, TestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int = 0

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

}