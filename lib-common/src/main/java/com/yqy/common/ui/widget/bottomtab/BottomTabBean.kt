package com.yqy.common.ui.widget.bottomtab

/**
 * @desc 底部tab信息实体类
 * @author derekyan
 * @date 2020/7/2
 */
data class BottomTabBean(
    var selectedIconResName: String = "",
    var unSelectedIconResId: Int = 0,
    var title: String = "",
    var selectedColor: Int = 0,
    var unSelectedColor: Int = 0,
    var isShowDot: Boolean = false,
    var isShowBadge: Boolean = false,
    var badgeNumber: Int = 0
)