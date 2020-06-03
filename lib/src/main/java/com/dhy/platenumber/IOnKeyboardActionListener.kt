package com.dhy.platenumber

import android.inputmethodservice.KeyboardView

interface IOnKeyboardActionListener : KeyboardView.OnKeyboardActionListener {
    override fun swipeUp() {}

    override fun swipeRight() {}

    override fun swipeLeft() {}

    override fun swipeDown() {}

    override fun onText(text: CharSequence) {}

    override fun onRelease(primaryCode: Int) {}

    override fun onPress(primaryCode: Int) {}
}