package com.fjjukic.zenvio.feature.home.data.repository

import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.home.model.PlanItem
import com.fjjukic.zenvio.feature.home.model.PlanType
import javax.inject.Inject
import javax.inject.Singleton

interface HomeRepository {
    fun breathingPlan(): PlanItem
    fun meditationPlan(): PlanItem
    fun journalPlan(): PlanItem
    fun articlePlan(): PlanItem
    fun gratitudeMeditationPlan(): PlanItem
    fun defaultPlan(): PlanItem

    fun getTodayPlans(): List<PlanItem>
}

@Singleton
class HomeRepositoryImpl @Inject constructor() : HomeRepository {

    override fun meditationPlan(): PlanItem {
        return PlanItem(
            id = "meditation_intro",
            type = PlanType.MEDITATION,
            titleRes = R.string.plan_intro_meditation,
            durationStart = 480, // 8 mins
            illustrationRes = R.drawable.img_meditation_intro
        )
    }

    override fun articlePlan(): PlanItem {
        return PlanItem(
            id = "article_mindfulness",
            type = PlanType.ARTICLE,
            titleRes = R.string.plan_mindfulness_techniques,
            durationStart = 120, // 2 mins read
            illustrationRes = R.drawable.img_mindfulness_techinques
        )
    }

    override fun breathingPlan(): PlanItem {
        return PlanItem(
            id = "breathing_deep",
            type = PlanType.BREATHING,
            titleRes = R.string.plan_deep_breath_dynamics,
            durationStart = 120,
            durationEnd = 300,
            illustrationRes = R.drawable.img_deep_breath
        )
    }

    override fun journalPlan(): PlanItem {
        return PlanItem(
            id = "journal_activities",
            type = PlanType.JOURNAL,
            titleRes = R.string.plan_smart_journal,
            durationStart = 20,
            durationEnd = 40,
            illustrationRes = R.drawable.img_activities
        )
    }

    override fun gratitudeMeditationPlan(): PlanItem {
        return PlanItem(
            id = "meditation_gratitude",
            type = PlanType.MEDITATION,
            titleRes = R.string.plan_gratitude_meditation,
            durationStart = 600, // 10 mins
            illustrationRes = R.drawable.img_gratitude_meditation
        )
    }

    override fun defaultPlan(): PlanItem {
        return meditationPlan()
    }

    override fun getTodayPlans(): List<PlanItem> {
        return listOf(
            meditationPlan(),
            articlePlan(),
            breathingPlan(),
            journalPlan(),
            gratitudeMeditationPlan(),
        )
    }
}