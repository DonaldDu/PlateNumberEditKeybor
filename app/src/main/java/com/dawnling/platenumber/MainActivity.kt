package com.dawnling.platenumber

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.dhy.platenumber.CarKeyboardUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var keyboardUtil: CarKeyboardUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyboardUtil = CarKeyboardUtil(this, editTexts, keyboard_view)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        keyboardUtil.hideKeyboard()
        return super.onTouchEvent(event)
    }
}
