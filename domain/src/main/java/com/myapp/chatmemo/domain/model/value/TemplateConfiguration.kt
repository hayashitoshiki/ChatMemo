package com.myapp.chatmemo.domain.model.value

import com.myapp.chatmemo.domain.model.entity.Template

/**
 * テンプレート設定定義
 */
data class TemplateConfiguration(
    /**
     * 使用するテンプレート
     */
    var template: Template,
    /**
     * テンプレートの表示形式
     */
    var templateMode: TemplateMode
)
