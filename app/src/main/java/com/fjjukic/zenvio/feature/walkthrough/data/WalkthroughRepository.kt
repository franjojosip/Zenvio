package com.fjjukic.zenvio.feature.walkthrough.data

import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.walkthrough.model.WalkthroughPage
import javax.inject.Inject

interface WalkthroughRepository {
    fun getPages(): List<WalkthroughPage>
}

class WalkthroughRepositoryImpl @Inject constructor() : WalkthroughRepository {
    override fun getPages(): List<WalkthroughPage> {
        return listOf(
            WalkthroughPage(
                imageRes = R.drawable.walkthrough_step_one,
                titleRes = R.string.title_walkthrough_step_one,
                descriptionRes = R.string.description_walkthrough_step_one
            ),
            WalkthroughPage(
                imageRes = R.drawable.walkthrough_step_two,
                titleRes = R.string.title_walkthrough_step_two,
                descriptionRes = R.string.description_walkthrough_step_two
            ),
            WalkthroughPage(
                imageRes = R.drawable.walkthrough_step_three,
                titleRes = R.string.title_walkthrough_step_three,
                descriptionRes = R.string.description_walkthrough_step_three
            ),
        )
    }
}