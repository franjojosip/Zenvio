package com.fjjukic.zenvio.feature.home.data.repository

import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.model.PlanItem
import com.fjjukic.zenvio.feature.home.model.PlanType
import javax.inject.Inject

interface HomeRepository {
    fun getTodayPlans(): List<PlanItem>
}

class HomeRepositoryImpl @Inject constructor() : HomeRepository {
    override fun getTodayPlans(): List<PlanItem> {
        return listOf(
            PlanItem(
                id = "1",
                type = PlanType.MEDITATION,
                titleRes = R.string.plan_intro_meditation,
                durationStart = 480,
                illustrationRes = R.drawable.img_meditation_intro
            ),
            PlanItem(
                id = "2",
                type = PlanType.ARTICLE,
                titleRes = R.string.plan_mindfulness_techniques,
                durationStart = 120,
                illustrationRes = R.drawable.img_mindfulness_techinques
            ),
            PlanItem(
                id = "3",
                type = PlanType.BREATHING,
                titleRes = R.string.plan_deep_breath_dynamics,
                durationStart = 120,
                durationEnd = 300,
                illustrationRes = R.drawable.img_deep_breath
            ),
            PlanItem(
                id = "4",
                type = PlanType.JOURNAL,
                titleRes = R.string.plan_smart_journal,
                durationStart = 20,
                durationEnd = 40,
                illustrationRes = R.drawable.img_activities
            ),
            PlanItem(
                id = "5",
                type = PlanType.MEDITATION,
                titleRes = R.string.plan_gratitude_meditation,
                durationStart = 600,
                illustrationRes = R.drawable.img_gratitude_meditation
            )
        )
    }

}