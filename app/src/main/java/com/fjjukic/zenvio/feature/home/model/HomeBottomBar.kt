package com.fjjukic.zenvio.feature.home.model

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fjjukic.zenvio.ui.defaults.AppInputDefaults
import com.fjjukic.zenvio.ui.theme.ZenvioTheme


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HomeBottomBarPreview() {
    ZenvioTheme {
        HomeBottomBar(
            selectedTab = HomeTab.HOME,
            onTabSelected = {}
        )
    }
}

@Composable
fun HomeBottomBar(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        HomeTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        painter = painterResource(tab.iconRes),
                        contentDescription = null,
                        tint = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            AppInputDefaults.navigationItemColor
                        }
                    )
                },
                label = {
                    Text(
                        text = stringResource(tab.labelRes),
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = AppInputDefaults.navigationItemColor,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = AppInputDefaults.navigationItemColor,
                    selectedIndicatorColor = Color.Transparent,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent
                ),

                interactionSource = remember { MutableInteractionSource() },
            )
        }
    }
}