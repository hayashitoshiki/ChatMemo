package com.myapp.chatmemo.ui.utils.expansion

import android.text.Spanned
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun setText(
    view: AutoCompleteTextView,
    text: CharSequence?
) {
    val oldText = view.text
    if (text === oldText || text == null && oldText.isEmpty()) {
        return
    }
    if (text is Spanned) {
        if (text == oldText) {
            return // No change in the spans, so don't set anything.
        }
    } else if (!haveContentsChanged(text, oldText)) {
        return // No content changes, so don't set anything.
    }
    view.setText(text, false)
}

private fun haveContentsChanged(
    str1: CharSequence?,
    str2: CharSequence?
): Boolean {
    if (str1 == null != (str2 == null)) {
        return true
    } else if (str1 == null) {
        return false
    }
    val length = str1.length
    if (length != str2!!.length) {
        return true
    }
    for (i in 0 until length) {
        if (str1[i] != str2[i]) {
            return true
        }
    }
    return false
}
