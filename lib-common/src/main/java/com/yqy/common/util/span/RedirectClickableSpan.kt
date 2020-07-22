package com.yqy.common.util.span

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class RedirectClickableSpan : ClickableSpan() {
    //设置链接颜色
    override fun onClick(widget: View) {}
    override fun updateDrawState(ds: TextPaint) {
        ds.color = Color.parseColor("#1E90FF")
    }
}