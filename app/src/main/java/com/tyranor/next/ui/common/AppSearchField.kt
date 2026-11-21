package com.tyranor.next.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tyranor.next.R

/**
 * 统一搜索 / 单行输入框：Material 3 OutlinedTextField（胶囊形、filled 容器色）。
 *
 * @param query 输入内容，状态由调用方持有
 * @param onQueryChange 输入变化回调
 * @param onSearch 键盘 IME 搜索动作回调
 * @param leadingIcon 前导图标；null 用默认搜索图标
 * @param iconContentDescription 无障碍标签 / 占位提示
 * @param textStyle 输入文字样式；null 用 bodyLarge
 */
@Composable
fun AppSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = { },
    leadingIcon: Painter? = null,
    leadingIconVector: ImageVector? = null,
    iconContentDescription: String? = null,
    textStyle: TextStyle? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Search,
    autofillContentType: ContentType? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val label = iconContentDescription
        ?: stringResource(R.string.common_search_content_description)
    val trailing: (@Composable () -> Unit)? = if (query.isNotEmpty()) {
        {
            IconButton(onClick = { onQueryChange("") }) {
                Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
    } else {
        null
    }
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = label },
        textStyle = textStyle ?: MaterialTheme.typography.bodyLarge,
        placeholder = { Text(label, style = MaterialTheme.typography.bodyLarge) },
        leadingIcon = {
            val iconModifier = Modifier.size(22.dp)
            val tint = MaterialTheme.colorScheme.onSurfaceVariant
            val vector = leadingIconVector ?: if (leadingIcon == null) AppIcons.search else null
            if (vector != null) {
                Icon(
                    imageVector = vector,
                    contentDescription = null,
                    tint = tint,
                    modifier = iconModifier,
                )
            } else {
                Icon(
                    painter = leadingIcon!!,
                    contentDescription = null,
                    tint = tint,
                    modifier = iconModifier,
                )
            }
        },
        trailingIcon = trailing,
        singleLine = true,
        shape = CircleShape,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }, onDone = { onSearch() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}
