package com.yqy.common.ui.widget.bottomtab

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.orhanobut.logger.Logger
import com.zs.common.R

/**
 * @desc
 * @author derekyan
 * @date 2020/7/2
 */
class BottomTabView : RelativeLayout {
    private val TAG = "BottomTabView"

    private lateinit var ll_tab: LinearLayout

    private val tabBeanList = mutableListOf<BottomTabBean>()
    private var tabWidth = 0
    private var tabHeight = 0
    private var screenWidth = 0

    private var selectedPosition = -1
    private var unSelectedPosition = -1

    private var mOnBottomTabChangedListener: OnBottomTabChangedListener? = null

    fun setOnBottomTabChangedListener(listener: OnBottomTabChangedListener) {
        mOnBottomTabChangedListener = listener
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        screenWidth = ScreenUtils.getAppScreenWidth()
        tabHeight = ConvertUtils.dp2px(60f)
        initRootView()
    }


    private fun initRootView() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_tab_view, this)
        ll_tab = view.findViewById(R.id.ll_tab)
        val lp = ll_tab.layoutParams
        lp.height = tabHeight
        ll_tab.layoutParams = lp
    }

    fun setTabBeanList(list: ArrayList<BottomTabBean>) {
        tabBeanList.clear()
        tabBeanList.addAll(list)
        tabWidth = screenWidth / list.size
        initTabs()
    }

    private fun initTabs() {
        ll_tab.removeAllViews()

        for (i in 0 until tabBeanList.size) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_bottom_tab_view, null)
            ll_tab.addView(view)

            view.setOnClickListener {
                selectedChanged(i)
            }

            val lp = view.layoutParams
            lp.width = tabWidth
            lp.height = tabHeight
            view.layoutParams = lp

            val bottomTabBean = tabBeanList[i]

            val iv_icon = view.findViewById<ImageView>(R.id.iv_icon)
            val tv_title = view.findViewById<TextView>(R.id.tv_title)
            val iv_tab_dot = view.findViewById<ImageView>(R.id.iv_tab_dot)
            val tv_tab_badge = view.findViewById<TextView>(R.id.tv_tab_badge)

            bottomTabBean.apply {
                if (unSelectedIconResId > 0) {
                    iv_icon.setImageResource(unSelectedIconResId)
                }
                tv_title.text = title
                tv_title.setTextColor(unSelectedColor)
                iv_tab_dot.visibility = if (isShowDot) View.VISIBLE else View.GONE
                tv_tab_badge.visibility =
                    if (isShowBadge) {
                        tv_tab_badge.text = badgeNumber.toString()
                        View.VISIBLE
                    } else View.GONE
            }
        }

        if (tabBeanList.isNotEmpty())
            selectedChanged(0)
    }

    fun select(selectedPosition: Int) {
        selectedChanged(selectedPosition)
    }

    private fun selectedChanged(selectedPosition: Int) {
        if (ll_tab.childCount != tabBeanList.size) {
            Logger.e("$TAG selectedChanged 失败")
            return
        }

        if (this.selectedPosition == selectedPosition) {
            return
        }

        unSelectedPosition = this.selectedPosition
        this.selectedPosition = selectedPosition

        for (i in 0 until tabBeanList.size) {
            val view = ll_tab.getChildAt(i)
            val iv_svg = view.findViewById<SVGAImageView>(R.id.iv_svg)
            val iv_icon = view.findViewById<ImageView>(R.id.iv_icon)
            val tv_title = view.findViewById<TextView>(R.id.tv_title)

            val bottomTabBean = tabBeanList[i]

            bottomTabBean.apply {
                // 选中
                if (selectedPosition == i) {
                    iv_icon.visibility = View.INVISIBLE
                    iv_svg.visibility = View.VISIBLE
                    if (selectedIconResName.isNotEmpty()) {
                        if (iv_svg.drawable != null) {
                            iv_svg.startAnimation()
                        } else {
                            val svgaParser = SVGAParser(context)
                            svgaParser.decodeFromAssets(
                                selectedIconResName,
                                object : SVGAParser.ParseCompletion {
                                    override fun onComplete(videoItem: SVGAVideoEntity) {
                                        val drawable = SVGADrawable(videoItem)
                                        iv_svg.setImageDrawable(drawable)
                                        iv_svg.startAnimation()
                                    }

                                    override fun onError() {
                                        Logger.e("$TAG  SVGAParser onError()")
                                    }
                                })
                        }
                    }
                    tv_title.setTextColor(selectedColor)

                } else {
                    iv_svg.visibility = View.INVISIBLE
                    iv_icon.visibility = View.VISIBLE
                    if (iv_svg.isAnimating) {
                        iv_svg.stopAnimation()
                    }
                    tv_title.setTextColor(unSelectedColor)
                }
            }
        }

        mOnBottomTabChangedListener?.onSelectedChanged(selectedPosition, unSelectedPosition)
    }

    fun setBadge(tabIndex: Int, number: Int) {
        if (tabIndex >= ll_tab.childCount) return
        val view = ll_tab.getChildAt(tabIndex)
        val tv_tab_badge = view.findViewById<TextView>(R.id.tv_tab_badge)
        tv_tab_badge.visibility =
            if (number > 0) {
                tv_tab_badge.text = number.toString()
                View.VISIBLE
            } else View.GONE
    }

    fun setDot(tabIndex: Int, isShow: Boolean) {
        if (tabIndex >= ll_tab.childCount) return
        val view = ll_tab.getChildAt(tabIndex)
        val iv_tab_dot = view.findViewById<TextView>(R.id.iv_tab_dot)
        iv_tab_dot.visibility =
            if (isShow) {
                View.VISIBLE
            } else View.GONE
    }

    interface OnBottomTabChangedListener {
        fun onSelectedChanged(selectedPosition: Int, unSelectedPosition: Int)
    }

}