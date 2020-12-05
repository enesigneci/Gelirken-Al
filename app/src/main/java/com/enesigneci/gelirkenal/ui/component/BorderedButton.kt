package com.enesigneci.gelirkenal.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.enesigneci.gelirkenal.R

class BorderedButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int): AppCompatButton(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    constructor(context: Context) : this(context, null, 0) {
        init()
    }
    private fun init() {
        background = ContextCompat.getDrawable(context, R.drawable.btn)
    }


}