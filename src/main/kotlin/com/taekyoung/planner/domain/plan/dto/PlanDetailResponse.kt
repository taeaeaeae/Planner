package com.taekyoung.planner.domain.plan.dto

import com.taekyoung.planner.domain.plan.comment.dto.CommentResponse

data class PlanDetailResponse(
    val plan: PlanResponse,
    val comment: List<CommentResponse>
)