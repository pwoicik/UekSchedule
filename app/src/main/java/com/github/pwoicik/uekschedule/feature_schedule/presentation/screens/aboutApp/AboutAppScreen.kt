package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.common.Constants
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp.components.AboutScreenScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp.components.Section
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp.components.inlineClickableEmail
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp.components.inlineClickableText
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.openInBrowser
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.openPlayStorePage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination
@Composable
fun AboutAppScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    AboutScreenScaffold(onNavigateBack = { navigator.navigateUp() }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Section(
                title = stringResource(R.string.about_section1_title),
                text = buildAnnotatedString {
                    append(stringResource(R.string.about_section1_part1) + " ")
                    appendInlineContent("google_play", stringResource(R.string.google_play))
                    append(".\n")

                    append(stringResource(R.string.about_section1_part2) + " ")
                    appendInlineContent(
                        "google_play_review",
                        stringResource(R.string.google_play_review)
                    )
                    append(" " + stringResource(R.string.about_section1_part3) + " ")
                    appendInlineContent("email", stringResource(R.string.email))
                    append(".")
                },
                inlineContent = mapOf(
                    inlineClickableEmail(context),
                    inlineClickableText(
                        id = "google_play",
                        text = stringResource(R.string.google_play),
                        onClickLabel = stringResource(R.string.google_play_page),
                        onClick = { context.openPlayStorePage() }
                    ),
                    inlineClickableText(
                        id = "google_play_review",
                        text = stringResource(R.string.google_play_review),
                        onClickLabel = stringResource(R.string.google_play_page),
                        onClick = { context.openPlayStorePage() }
                    )
                )
            )
            Section(
                title = stringResource(R.string.about_section2_title),
                text = buildAnnotatedString {
                    append(stringResource(R.string.about_section2_part1) + " ")
                    appendInlineContent("github", stringResource(R.string.github))
                    append(".\n")

                    append(stringResource(R.string.about_section2_part2) + " ")
                    appendInlineContent("email", stringResource(R.string.email))
                    append(".")
                },
                inlineContent = mapOf(
                    inlineClickableEmail(context),
                    inlineClickableText(
                        id = "github",
                        text = stringResource(R.string.github),
                        onClickLabel = stringResource(R.string.github_page),
                        onClick = { context.openInBrowser(Constants.GITHUB_REPO_URL) }
                    )
                )
            )
        }
    }
}

@Composable
fun ClickableText(
    text: String,
    onClickLabel: String,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.clickable(
            onClickLabel = onClickLabel,
            onClick = onClick
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    MaterialTheme.colorScheme.secondaryContainer.copy(
                        alpha = 0.9f
                    )
                )
        )
        Text(text)
    }
}
