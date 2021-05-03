package com.myapp.chatmemo.domain.model.value

import com.myapp.chatmemo.domain.model.entity.Template

data class TemplateConfiguration(
    var template: Template, var templateMode: TemplateMode
)
