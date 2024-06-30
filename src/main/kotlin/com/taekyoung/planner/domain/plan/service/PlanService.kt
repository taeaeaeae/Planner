package com.taekyoung.planner.domain.plan.service

import com.taekyoung.planner.domain.plan.dto.PlanDetailResponse
import com.taekyoung.planner.domain.plan.dto.PlanRequest
import com.taekyoung.planner.domain.plan.dto.PlanResponse
import com.taekyoung.planner.domain.plan.dto.UpdatePlanRequest
import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.model.SearchType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PlanService {
    fun getPlanAll(
        pageable: Pageable,
        order: String?,
        type: SearchType?,
        keyword: String?,
        status: PlanStatus?
    ): Page<PlanResponse>

    fun getPlanById(planId: Long): PlanDetailResponse

    fun createPlan(request: PlanRequest, memberId: Long): PlanResponse

    fun updatePlan(planId: Long, memberId: Long, request: UpdatePlanRequest): PlanResponse

    fun deletePlan(planId: Long, memberId: Long)
}