package com.fjjukic.zenvio.core.model

data class UserPreferences(
    val name: String = "",
    val gender: String = "",
    val age: Int = 25,
    val goals: List<String> = emptyList(),
    val mentalHealthIssues: List<String> = emptyList(),
    val stressFrequency: String = "",
    val healthyEating: String = "",
    val meditationExperience: String = "",
    val sleepQuality: String = "",
    val happiness: String = ""
)
