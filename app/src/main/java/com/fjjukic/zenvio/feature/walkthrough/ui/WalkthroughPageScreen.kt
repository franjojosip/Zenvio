package com.fjjukic.zenvio.feature.walkthrough.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughPage


@Preview
@Composable
fun WalkthroughPageScreenPreview() {
    WalkthroughPageScreen(
        WalkthroughPage(
            imageRes = R.drawable.walkthrough_step_one,
            titleRes = R.string.title_walkthrough_step_one,
            descriptionRes = R.string.description_walkthrough_step_one
        )
    )
}

@Composable
fun WalkthroughPageScreen(
    page: WalkthroughPage
) {
    var componentHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .onGloballyPositioned {
                componentHeight = with(density) {
                    it.size.height.toDp() * 0.72f
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = componentHeight)
                .clip(CurvedBottomShape())
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(page.titleRes),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(page.descriptionRes),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

class CurvedBottomShape(
    private val curveHeight: Float = 120f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height - curveHeight)

            // Slight left→right curve
            quadraticTo(
                width / 2, height + curveHeight,
                width, height - curveHeight
            )

            lineTo(width, 0f)
            close()
        }

        return Outline.Generic(path)
    }
}
