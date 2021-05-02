package com.example.chatmemo.domain.model.value

import com.example.chatmemo.domain.model.entity.Template

data class TemplateConfiguration(
    var template: Template,
    var templateMode: TemplateMode
)
