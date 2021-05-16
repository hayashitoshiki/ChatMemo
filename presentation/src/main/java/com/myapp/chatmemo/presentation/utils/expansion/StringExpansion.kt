package com.myapp.chatmemo.presentation.utils.expansion

import androidx.annotation.StringRes
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateMode
import com.myapp.chatmemo.presentation.R

@get: StringRes
val TemplateMode.text: Int
    get() = when (this) {
        is TemplateMode.Order -> {
            R.string.template_mode_order
        }
        is TemplateMode.Randam -> {
            R.string.template_mode_randam
        }
    }

@get: StringRes
val Template.firsText: Int
    get() = R.string.spinner_message_first
