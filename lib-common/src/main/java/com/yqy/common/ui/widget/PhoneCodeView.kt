package com.yqy.common.ui.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.zs.common.R
import java.lang.StringBuilder


/**
 * @desc 验证码输入框
 * @author derekyan
 * @date 2020/7/2
 */
class PhoneCodeView : RelativeLayout {
    private val TAG = "PhoneCodeView"

//    private lateinit var imm: InputMethodManager
    private lateinit var et_code: EditText
    private lateinit var ll_code: LinearLayout

    private val codes = mutableListOf<String>()
    private var codeLength = 4

    private var screenWidth = 0
    private var parentWidth = 0


    private var mOnInputCompleteListener: OnInputCompleteListener? = null

    fun setOnInputCompleteListener(listener: OnInputCompleteListener) {
        this.mOnInputCompleteListener = listener
    }

    constructor(context: Context?) : super(context) {
        loadView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        loadView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        loadView()
    }

    private fun loadView() {
//        imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        screenWidth = ScreenUtils.getScreenWidth()
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_phone_code_view, this)
        initView(view)
        initEvent()
    }

    private fun initView(view: View) {
        et_code = view.findViewById(R.id.et_code)
        ll_code = view.findViewById(R.id.ll_code)



        ll_code.removeAllViews()

        parentWidth = measuredWidth
        // 计算margin
        val itemWidth = ConvertUtils.dp2px(60f)
        val oldMargin =  ConvertUtils.dp2px(18 * 2f)
        val margin = (screenWidth - (itemWidth * 4)) / 3f / 2

        println("$TAG parentWidth=$parentWidth   screenWidth=$screenWidth  itemWidth=$itemWidth  margin=$margin ${ConvertUtils.dp2px(margin.toFloat())} oldMargin=$oldMargin")

        // 动态设置margin
        for (i in 0 until codeLength) {
            LayoutInflater.from(context).inflate(R.layout.item_phone_code_view, ll_code)
            val lp = ll_code.getChildAt(i).layoutParams as LinearLayout.LayoutParams
            when (i) {
                0 -> {
                }
                else -> {
                    lp.marginStart = margin.toInt()
                }
            }
            ll_code.getChildAt(i).layoutParams = lp
        }
        et_code.filters = arrayOf<InputFilter>(LengthFilter(codeLength))
        et_code.requestFocus()
    }

    private fun initEvent() {
        //验证码输入
        et_code.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.isNotEmpty()) {
                    et_code.setText("")
                    if (codes.size < codeLength) {
                        val data = editable.toString().trim { it <= ' ' }
                        codes.add(data)
                        showCode()
                    }

                    // 输入完成 回调监听
                    if (codes.size == codeLength && mOnInputCompleteListener != null) {
                        val sb = StringBuilder()
                        codes.forEach { sb.append(it) }
                        mOnInputCompleteListener?.onInputComplete(sb.toString())
                    }
                }
            }
        })

        // 监听验证码删除按键
        et_code.setOnKeyListener(OnKeyListener { view, keyCode, keyEvent ->
            if (codes.isNotEmpty()) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action === KeyEvent.ACTION_DOWN && codes.size > 0) {
                    codes.removeAt(codes.size - 1)
                    showCode()
                    return@OnKeyListener true
                }
            }
            false
        })
    }

    private fun showCode() {
        for (i in 0 until ll_code.childCount) {
            (ll_code.getChildAt(i) as TextView).text = ""
        }
        for (i in 0 until codes.size) {
            (ll_code.getChildAt(i) as TextView).text = codes[i]
        }
    }

    // 呼出软键盘
    fun requestFocus1() {
        et_code.requestFocus()
        KeyboardUtils.showSoftInput(et_code)
    }

    /**
     * 输入完成监听
     */
    interface OnInputCompleteListener {
        fun onInputComplete(code: String)
    }
}