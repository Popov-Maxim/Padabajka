import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.TextUnit
import com.padabajka.dating.core.networking.NetworkConstants
import com.padabajka.dating.core.presentation.ui.dictionary.languageCode

@Composable
fun FormattedLegalText(
    textPattern: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        ),
        pressedStyle = SpanStyle(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    )

    val annotatedText = parseLegalTextPattern(textPattern, linkStyles, onLinkClick)

    Text(
        text = annotatedText,
        modifier = modifier,
        fontSize = fontSize,
        style = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

fun parseLegalTextPattern(
    textPattern: String,
    linkStyles: TextLinkStyles,
    onLinkClick: (String) -> Unit
): AnnotatedString {
    val regex = """\[([^\]]+)\]\(([^)]+)\)""".toRegex()

    return buildAnnotatedString {
        var lastIndex = 0

        regex.findAll(textPattern).forEach { matchResult ->
            val textBefore = textPattern.substring(lastIndex, matchResult.range.first)
            append(textBefore)

            val linkText = matchResult.groupValues[1]
            val linkTag = matchResult.groupValues[2]

            withLink(
                link = LinkAnnotation.Clickable(
                    tag = linkTag,
                    styles = linkStyles,
                    linkInteractionListener = { annotation ->
                        val tag = (annotation as? LinkAnnotation.Clickable)?.tag
                        if (tag != null) {
                            onLinkClick(tag)
                        }
                    }
                )
            ) {
                append(linkText)
            }

            lastIndex = matchResult.range.last + 1
        }

        if (lastIndex < textPattern.length) {
            append(textPattern.substring(lastIndex))
        }
    }
}

@Composable
fun defaultOnLinkClick(terms: String, privacy: String): (String) -> Unit {
    val uriHandler = LocalUriHandler.current
    val langCode = languageCode()
    return { tag ->
        val version = when (tag) {
            "terms" -> terms
            "privacy" -> privacy
            else -> TODO()
        }
        val url = NetworkConstants.path + "/legal/$langCode/$version.pdf"

        uriHandler.openUri(url)
    }
}

@Composable
fun openLegalInBrowser(version: String): () -> Unit {
    val uriHandler = LocalUriHandler.current
    val langCode = languageCode()
    return {
        val url = NetworkConstants.path + "/legal/$langCode/$version.pdf"
        uriHandler.openUri(url)
    }
}
