package com.fjjukic.zenvio.feature.home.model

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.PlanItem
import com.fjjukic.zenvio.feature.home.PlanType
import com.fjjukic.zenvio.ui.formatters.formatPlantDuration
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun FirstTimelineItemPreview() {
    ZenvioTheme {
        PlanTimelineItem(
            PlanItem(
                id = "1",
                type = PlanType.MEDITATION,
                titleRes = R.string.plan_intro_meditation,
                durationStart = 480,
                illustrationRes = R.drawable.img_meditation_intro
            ),
            isFirst = true,
            isLast = false
        ) {}
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MiddleTimelineItemPreview() {
    ZenvioTheme {
        PlanTimelineItem(
            PlanItem(
                id = "1",
                type = PlanType.MEDITATION,
                titleRes = R.string.plan_intro_meditation,
                durationStart = 480,
                illustrationRes = R.drawable.img_meditation_intro
            ),
            isFirst = false,
            isLast = false
        ) {}
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LastTimelineItemPreview() {
    ZenvioTheme {
        PlanTimelineItem(
            PlanItem(
                id = "1",
                type = PlanType.MEDITATION,
                titleRes = R.string.plan_intro_meditation,
                durationStart = 480,
                illustrationRes = R.drawable.img_meditation_intro
            ),
            isFirst = false,
            isLast = true
        ) {}
    }
}

@Composable
fun PlanTimelineItem(
    plan: PlanItem,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val durationText = formatPlantDuration(context, plan.durationStart, plan.durationEnd)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight(),  // Now this is valid
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                if (!isFirst) {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(size.width / 2f, 0f),
                        end = Offset(size.width / 2f, size.height / 2),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(18f, 18f),
                            0f
                        )
                    )
                }

                if (!isLast) {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(size.width / 2f, size.height / 2),
                        end = Offset(size.width / 2f, size.height),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(18f, 18f),
                            0f
                        )
                    )
                }
            }

            Canvas(modifier = Modifier.size(20.dp)) {
                drawCircle(
                    color = primaryColor,
                    radius = size.minDimension / 2,
                )
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension - 12.dp.toPx()
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .height(96.dp)
                .padding(start = 16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(plan.type.labelRes),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = stringResource(plan.titleRes),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = durationText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF8E96A3)
                        )
                    )
                }

                Spacer(Modifier.width(8.dp))

                Image(
                    painter = painterResource(plan.illustrationRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun VerticalDashedLinePreview() {
    ZenvioTheme {
        VerticalDashedLine()
    }
}

@Composable
fun VerticalDashedLine(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    dashLength: Float = 12f,
    gapLength: Float = 8f,
    strokeWidth: Float = 4f
) {
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(x = size.width / 2, y = 0f),
            end = Offset(x = size.width / 2, y = size.height),
            strokeWidth = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength, gapLength), 0f
            )
        )
    }
}
