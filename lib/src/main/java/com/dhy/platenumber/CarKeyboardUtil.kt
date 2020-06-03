package com.dhy.platenumber

import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class CarKeyboardUtil(private val mActivity: Activity, private val editTexts: ViewGroup, keyboardView: KeyboardView) {
    private val list: List<EditText> = getEditTexts()
    private val mKeyboardView: KeyboardView = keyboardView
    /**
     * 省份简称键盘
     */
    private val province_keyboard: Keyboard
    /**
     * 数字与大写字母键盘
     */
    private val have_chinese_keyboar: Keyboard
    private val english_keyboar: Keyboard
    private val without_chinese_keyboar: Keyboard
    private fun getEditTexts(): List<EditText> {
        val list: MutableList<EditText> = mutableListOf()
        for (i in 0 until editTexts.childCount) {
            list.add(editTexts.getChildAt(i) as EditText)
        }
        return list
    }

    private val listener = object : IOnKeyboardActionListener {
        override fun onKey(primaryCode: Int, keyCodes: IntArray) {
            val pos = getPosition()
            val et = list[pos]
            if (primaryCode == -1) {
                hideKeyboard()
            } else if (primaryCode == -3) {// 回退
                if (isNotEmpty()) {
                    if (et.length() > 0) et.setText("")
                    else {
                        val pre = list[pos - 1]
                        pre.setText("")
                        pre.requestFocus()
                        showKeyboard(pos - 1)
                    }
                }
            } else {
                et.setText(primaryCode.toChar().toString())
                showKeyboard(pos + 1)
                if (pos != list.lastIndex) {
                    val next = list[pos + 1]
                    next.requestFocus()
                }
            }
        }
    }

    private fun isNotEmpty(): Boolean {
        return list.find { it.length() > 0 } != null
    }

    private fun getPosition(): Int {
        return list.indexOfFirst { it.isFocused }
    }

    /**
     * 软键盘展示状态
     */
    private val isShow: Boolean
        get() = mKeyboardView.visibility == View.VISIBLE


    private var openAnimation: Animation? = null
    private var closeAnimation: Animation? = null

    private var onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            var et = v as EditText
            if (et.length() == 0) {
                et = list.find { it.length() == 0 }!!
                if (et != v) et.requestFocus()
            }
            showKeyboard(list.indexOf(et))
            showKeyboard()
        } else {
            val focused = getPosition()
            if (focused == -1) hideKeyboard()
        }
    }

    init {
        init()
        list.last().hint = "新"
        list.forEach {
            it.inputType = InputType.TYPE_NULL
            it.onFocusChangeListener = onFocusChangeListener
        }
        province_keyboard = Keyboard(mActivity, R.xml.provice)//省份简称
        english_keyboar = Keyboard(mActivity, R.xml.english)//第二位  纯英文o 不允许数字
        without_chinese_keyboar = Keyboard(mActivity, R.xml.qwerty_whitout_chinese)//第三位 数字+英文o 不允许港 澳 学
        have_chinese_keyboar = Keyboard(mActivity, R.xml.qwerty_have_chinese)//最后一位 数字+英文+港澳学  不允许o

        mKeyboardView.keyboard = province_keyboard
        mKeyboardView.isEnabled = true
        mKeyboardView.isPreviewEnabled = false
        mKeyboardView.setOnKeyboardActionListener(listener)
    }

    private fun showKeyboard(position: Int) {
        when (position) {
            0 -> mKeyboardView.keyboard = province_keyboard
            1 -> mKeyboardView.keyboard = english_keyboar
            in 2..6 -> mKeyboardView.keyboard = without_chinese_keyboar
            7 -> mKeyboardView.keyboard = have_chinese_keyboar
        }
    }


    /**
     * 软键盘展示
     */
    private fun showKeyboard() {
        hideSystemKeyBroad()
        if (!isShow) {
            mKeyboardView.visibility = View.VISIBLE
            startAnimation()
            onKeyboardListener?.onKeyboard(true)
        }
    }

    /**
     * 软键盘隐藏
     */
    fun hideKeyboard() {
        if (isShow) {
            stopAnimation()
            mKeyboardView.visibility = View.GONE
            onKeyboardListener?.onKeyboard(false)
        }
    }

    /**
     * 隐藏系统软键盘
     */
    private fun hideSystemKeyBroad() {
        (mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(mActivity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun init() {
        openAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.anim_entry_from_bottom)
        closeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.anim_leave_from_bottom)
    }

    private fun startAnimation() {
        if (openAnimation != null) {
            mKeyboardView.startAnimation(openAnimation)
        }
    }

    private fun stopAnimation() {
        if (closeAnimation != null) {
            mKeyboardView.startAnimation(closeAnimation)
        }
    }

    var onKeyboardListener: OnKeyboardListener? = null

    interface OnKeyboardListener {
        /**
         * @param show true:show, false:hide
         * */
        fun onKeyboard(show: Boolean)
    }
}
