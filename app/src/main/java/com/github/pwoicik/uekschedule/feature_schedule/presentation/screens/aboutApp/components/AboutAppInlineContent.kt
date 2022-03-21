package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp.components

import android.content.Context
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp.ClickableText
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.openComposeEmailToSupport

fun inlineClickableEmail(context: Context) = inlineClickableText(
    id = "email",
    text = context.resources.getString(R.string.email),
    onClickLabel = "Compose email",
    onClick = { context.openComposeEmailToSupport() }
)

fun inlineClickableText(
    id: String,
    text: String,
    onClickLabel: String,
    onClick: () -> Unit
) = id to InlineTextContent(
    placeholder = Placeholder(
        width = 8.sp * (text.length + 2),
        height = 22.sp,
        placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom
    )
) {
    ClickableText(
        text = text,
        onClickLabel = onClickLabel,
        onClick = onClick
    )
}
