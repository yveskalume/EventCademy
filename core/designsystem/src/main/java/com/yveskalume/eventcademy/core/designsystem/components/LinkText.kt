package com.yveskalume.eventcademy.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withAnnotation

private const val LinkTag = "Link"
val ClickLink = SemanticsPropertyKey<AccessibilityAction<(index: Int) -> Boolean>>(
    name = "ClickLink",
    mergePolicy = { parentValue, childValue ->
        AccessibilityAction(
            parentValue?.label ?: childValue.label,
            parentValue?.action ?: childValue.action
        )
    }
)

fun SemanticsPropertyReceiver.onClickLink(
    label: String? = null,
    action: ((Int) -> Boolean)?
) {
    this[ClickLink] = AccessibilityAction(label, action)
}

@Composable
fun LinkedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = LocalContentColor.current,
    style: TextStyle = LocalTextStyle.current,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    linkColors: LinkColors = LinkedTextDefaults.linkColors(),
    onClick: (String) -> Unit
) {
    var layoutResult: TextLayoutResult? by remember { mutableStateOf(null) }
    var pressedIndex: Int by remember { mutableStateOf(-1) }
    val annotations = text.getLinkAnnotations(0, text.length)
    val styledText = AnnotatedString.Builder(text).apply {
        for ((i, ann) in annotations.withIndex()) {
            val textColor = linkColors.textColor(enabled, i == pressedIndex).value
            val backgroundColor = linkColors.backgroundColor(enabled, i == pressedIndex).value
            addStyle(
                SpanStyle(color = textColor, background = backgroundColor),
                start = ann.start,
                end = ann.end
            )
        }
    }.toAnnotatedString()
    val pressIndicator = Modifier.pointerInput(onClick) {
        detectTapGestures(
            onPress = { pos ->
                layoutResult?.getOffsetForPosition(pos)?.let { offset ->
                    val index = annotations.indexOfFirst {
                        it.start <= offset && it.end >= offset
                    }
                    pressedIndex = index
                    if (index >= 0) {
                        tryAwaitRelease()
                        pressedIndex = -1
                    }
                }
            },
            onTap = { pos ->
                layoutResult?.getOffsetForPosition(pos)?.let { offset ->
                    annotations.firstOrNull { it.start <= offset && it.end >= offset }
                        ?.item
                        ?.let { url -> onClick(url.toString()) }
                }
            }
        )
    }
    val actionSemantics = Modifier.semantics {
        onClickLink { index ->
            annotations.getOrNull(index)?.let {
                onClick(it.item.toString())
            }
            true
        }
    }
    Text(
        text = styledText,
        modifier = modifier
            .then(pressIndicator)
            .then(actionSemantics),
        color = color,
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = { result ->
            layoutResult = result
            onTextLayout(result)
        }
    )
}

fun <R : Any> AnnotatedString.Builder.withLink(
    url: String,
    block: AnnotatedString.Builder.() -> R
): R = withAnnotation(LinkTag, url, block)

object LinkedTextDefaults {
    @Composable
    fun linkColors(
        textColor: Color = com.yveskalume.eventcademy.core.designsystem.theme.Blue200,
        disabledTextColor: Color = textColor,
        pressedTextColor: Color = textColor,
        backgroundColor: Color = Color.Unspecified,
        disabledBackgroundColor: Color = backgroundColor,
        pressedBackgroundColor: Color = textColor.copy(
            alpha = 0.2f
        )
    ): LinkColors = DefaultLinkColors(
        textColor = textColor,
        disabledTextColor = disabledTextColor,
        pressedTextColor = pressedTextColor,
        backgroundColor = backgroundColor,
        disabledBackgroundColor = disabledBackgroundColor,
        pressedBackgroundColor = pressedBackgroundColor
    )
}

interface LinkColors {
    @Composable
    fun textColor(enabled: Boolean, isPressed: Boolean): State<Color>

    @Composable
    fun backgroundColor(enabled: Boolean, isPressed: Boolean): State<Color>
}

@Immutable
private data class DefaultLinkColors(
    private val textColor: Color,
    private val disabledTextColor: Color,
    private val pressedTextColor: Color,
    private val backgroundColor: Color,
    private val disabledBackgroundColor: Color,
    private val pressedBackgroundColor: Color
) : LinkColors {
    @Composable
    override fun textColor(enabled: Boolean, isPressed: Boolean): State<Color> =
        animateColorAsState(
            when {
                !enabled -> disabledTextColor
                isPressed -> pressedTextColor
                else -> textColor
            }, label = ""
        )

    @Composable
    override fun backgroundColor(enabled: Boolean, isPressed: Boolean): State<Color> =
        animateColorAsState(
            when {
                !enabled -> disabledBackgroundColor
                isPressed -> pressedBackgroundColor
                else -> backgroundColor
            }, label = ""
        )
}