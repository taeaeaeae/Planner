package com.taekyoung.planner.domain.plan.dto

import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.model.Plan
import java.time.LocalDateTime

data class PlanResponse(
    val planId: Long,
    val title: String,
    val content: String,
    val status: PlanStatus,
    val writer: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)

fun Plan.toResponse(): PlanResponse {
    return PlanResponse(
        planId = id!!,
        title = title,
        content = content,
        status = status,
        writer = member.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}