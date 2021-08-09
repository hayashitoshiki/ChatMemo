package com.myapp.chatmemo.domain.model.value

import com.myapp.chatmemo.domain.model.entity.Template

/**
 * テンプレート設定定義
 *
 * @property template 使用するテンプレート
 * @property templateMode テンプレートの表示形式
 */
data class TemplateConfiguration(
    var template: Template,
    var templateMode: TemplateMode
)
