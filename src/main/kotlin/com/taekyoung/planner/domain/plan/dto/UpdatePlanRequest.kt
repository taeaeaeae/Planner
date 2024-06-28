package com.taekyoung.planner.domain.plan.dto

import com.taekyoung.planner.domain.plan.model.PlanStatus

data class UpdatePlanRequest(
    val title: String,
    val content: String,
    val status: PlanStatus
)