package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackdropBottom
import com.example.ui.theme.DarkBackdropMid
import com.example.ui.theme.DarkBackdropTop
import com.example.ui.theme.DarkGlassBorderBottom
import com.example.ui.theme.DarkGlassBorderTop
import com.example.ui.theme.DarkOrbIndigo
import com.example.ui.theme.DarkOrbSky
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkSurfaceGlassSelected
import com.example.ui.theme.DarkSurfaceGlassSubtle
import com.example.ui.theme.IndigoDeep
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.LightBackdropBottom
import com.example.ui.theme.LightBackdropMid
import com.example.ui.theme.LightBackdropTop
import com.example.ui.theme.LightGlassBorderBottom
import com.example.ui.theme.LightGlassBorderTop
import com.example.ui.theme.LightOrbIndigo
import com.example.ui.theme.LightOrbPeriwinkle
import com.example.ui.theme.LightOrbSky
import com.example.ui.theme.LightSurfaceGlass
import com.example.ui.theme.LightSurfaceGlassSelected
import com.example.ui.theme.LightSurfaceGlassSubtle
import com.example.ui.theme.PrioritySkyBg
import com.example.ui.theme.PrioritySkyDarkBg
import com.example.ui.theme.PrioritySkyDarkText
import com.example.ui.theme.PrioritySkyText
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SuccessEmeraldBg
import com.example.ui.theme.SuccessEmeraldDarkBg
import com.example.ui.theme.SuccessEmeraldDarkText
import com.example.ui.theme.SuccessEmeraldText
import com.example.ui.theme.WarningAmberBg
import com.example.ui.theme.WarningAmberDarkBg
import com.example.ui.theme.WarningAmberDarkText
import com.example.ui.theme.WarningAmberText

/**
 * Single source of truth for SafarGo Glassmorphism design tokens.
 */
object GlassTokens {
    val CardRadius = 20.dp
    val SheetRadius = 28.dp
    val ControlRadius = 14.dp
    val PillRadius = 999.dp
    val BackdropOrbBlur = 52.dp
    val DefaultElevation = 4.dp
    val SelectedElevation = 8.dp
    val NavElevation = 8.dp
    val CardPadding = 16.dp
    val SectionSpacing = 16.dp
}

enum class GlassNoticeTone {
    INFO, WARNING, SUCCESS, ERROR
}

/**
 * Atmospheric multi-layer Glassmorphism backdrop featuring soft blurred indigo & sky radial orbs
 * behind translucent foreground surfaces. Foreground text remains completely sharp.
 */
@Composable
fun SafarGoGlassBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val backdropBrush = if (isDark) {
        Brush.verticalGradient(listOf(DarkBackdropTop, DarkBackdropMid, DarkBackdropBottom))
    } else {
        Brush.verticalGradient(listOf(LightBackdropTop, LightBackdropMid, LightBackdropBottom))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backdropBrush)
    ) {
        // Blurred ambient color layer behind glass panels for depth & refraction
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(GlassTokens.BackdropOrbBlur)
        ) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .offset(x = (-64).dp, y = (-48).dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                (if (isDark) DarkOrbIndigo else LightOrbIndigo).copy(alpha = if (isDark) 0.42f else 0.24f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 72.dp, y = 110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                (if (isDark) DarkOrbSky else LightOrbSky).copy(alpha = if (isDark) 0.34f else 0.26f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-70).dp, y = 80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                (if (isDark) DarkOrbIndigo else LightOrbPeriwinkle).copy(alpha = if (isDark) 0.30f else 0.20f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        content()
    }
}

@Composable
fun glassBorderBrush(
    isSelected: Boolean = false,
    isDark: Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f
): Brush {
    return when {
        isSelected && isDark -> Brush.linearGradient(
            listOf(SkyBlueAccent.copy(alpha = 0.85f), MaterialTheme.colorScheme.primary.copy(alpha = 0.55f))
        )
        isSelected && !isDark -> Brush.linearGradient(
            listOf(IndigoPrimary.copy(alpha = 0.85f), SkyBlueAccent.copy(alpha = 0.70f))
        )
        isDark -> Brush.linearGradient(
            listOf(DarkGlassBorderTop, DarkGlassBorderBottom)
        )
        else -> Brush.linearGradient(
            listOf(LightGlassBorderTop, LightGlassBorderBottom)
        )
    }
}

@Composable
fun glassFillColor(
    isSelected: Boolean = false,
    isDark: Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f,
    subtle: Boolean = false
): Color {
    return when {
        isDark && isSelected -> DarkSurfaceGlassSelected
        isDark && subtle -> DarkSurfaceGlassSubtle
        isDark -> DarkSurfaceGlass
        isSelected -> LightSurfaceGlassSelected
        subtle -> LightSurfaceGlassSubtle
        else -> LightSurfaceGlass
    }
}

@Composable
fun glassSpecularBrush(
    isDark: Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f
): Brush {
    return if (isDark) {
        Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.08f), Color.Transparent)
        )
    } else {
        Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.45f), Color.White.copy(alpha = 0.05f))
        )
    }
}

/**
 * Core translucent Glassmorphism surface used by cards, banners, sheets, bars, and tiles.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(GlassTokens.CardRadius),
    isSelected: Boolean = false,
    subtle: Boolean = false,
    elevation: Dp = GlassTokens.DefaultElevation,
    containerColor: Color? = null,
    border: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val resolvedFill = containerColor ?: glassFillColor(isSelected = isSelected, isDark = isDark, subtle = subtle)
    val resolvedBorder = border ?: BorderStroke(
        width = if (isSelected) 1.5.dp else 1.dp,
        brush = glassBorderBrush(isSelected = isSelected, isDark = isDark)
    )
    val resolvedElevation = if (isSelected) GlassTokens.SelectedElevation else elevation

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = resolvedFill,
            border = resolvedBorder,
            shadowElevation = resolvedElevation
        ) {
            Box(
                modifier = Modifier.background(glassSpecularBrush(isDark)),
                content = content
            )
        }
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = resolvedFill,
            border = resolvedBorder,
            shadowElevation = resolvedElevation
        ) {
            Box(
                modifier = Modifier.background(glassSpecularBrush(isDark)),
                content = content
            )
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(GlassTokens.CardRadius),
    isSelected: Boolean = false,
    subtle: Boolean = false,
    elevation: Dp = GlassTokens.DefaultElevation,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassSurface(
        modifier = modifier,
        shape = shape,
        isSelected = isSelected,
        subtle = subtle,
        elevation = elevation,
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

@Composable
fun GlassIndigoHeroBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(GlassTokens.CardRadius),
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.34f), SkyBlueAccent.copy(alpha = 0.28f))
            )
        ),
        shadowElevation = GlassTokens.SelectedElevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(IndigoDeep.copy(alpha = 0.94f), IndigoPrimary.copy(alpha = 0.90f))
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(SkyBlueAccent.copy(alpha = 0.18f), Color.Transparent)
                        )
                    )
            )
            content()
        }
    }
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(GlassTokens.ControlRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = IndigoPrimary,
            contentColor = Color.White,
            disabledContainerColor = IndigoPrimary.copy(alpha = 0.55f),
            disabledContentColor = Color.White.copy(alpha = 0.85f)
        ),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.35f), SkyBlueAccent.copy(alpha = 0.25f))
            )
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 1.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
        } else if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontSize = 15.sp
        )
    }
}

@Composable
fun GlassSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color? = null,
    content: @Composable RowScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(GlassTokens.ControlRadius),
        border = if (borderColor != null) {
            BorderStroke(1.dp, borderColor)
        } else {
            BorderStroke(1.dp, glassBorderBrush(isDark = isDark))
        },
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = glassFillColor(isDark = isDark, subtle = true),
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        content = content
    )
}

@Composable
fun GlassInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val containerColor = glassFillColor(isDark = isDark, subtle = true)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        shape = RoundedCornerShape(GlassTokens.ControlRadius),
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = glassFillColor(isSelected = true, isDark = isDark),
            unfocusedContainerColor = containerColor,
            errorContainerColor = containerColor,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = if (isDark) Color.White.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.85f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun GlassNoticeBanner(
    text: String,
    tone: GlassNoticeTone = GlassNoticeTone.INFO,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val (bg, fg, icon) = when (tone) {
        GlassNoticeTone.INFO -> Triple(
            if (isDark) PrioritySkyDarkBg else PrioritySkyBg,
            if (isDark) PrioritySkyDarkText else PrioritySkyText,
            Icons.Filled.Info
        )
        GlassNoticeTone.WARNING -> Triple(
            if (isDark) WarningAmberDarkBg else WarningAmberBg,
            if (isDark) WarningAmberDarkText else WarningAmberText,
            Icons.Filled.WarningAmber
        )
        GlassNoticeTone.SUCCESS -> Triple(
            if (isDark) SuccessEmeraldDarkBg else SuccessEmeraldBg,
            if (isDark) SuccessEmeraldDarkText else SuccessEmeraldText,
            Icons.Filled.CheckCircle
        )
        GlassNoticeTone.ERROR -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            Icons.Filled.ErrorOutline
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(GlassTokens.ControlRadius),
        color = bg,
        border = BorderStroke(1.dp, fg.copy(alpha = 0.28f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier
                    .padding(top = 1.dp)
                    .size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = fg
            )
        }
    }
}
