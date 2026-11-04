package com.padabajka.dating.settings.presentation.model.support

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

enum class SupportCategory(val tag: String, val title: StaticTextId.UiId) {
    Bug("BUG", StaticTextId.UiId.SupportCategoryBug),
    Report("REPORT", StaticTextId.UiId.SupportCategoryReport),
    Payment("PAYMENT", StaticTextId.UiId.SupportCategoryPayment),
    Account("ACCOUNT", StaticTextId.UiId.SupportCategoryAccount),
    Other("OTHER", StaticTextId.UiId.SupportCategoryOther),
}
