package com.taekyoung.planner.domain.plan.repository

import com.taekyoung.planner.domain.plan.model.Plan
import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.model.SearchType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomPlanRepository {
    fun findByAll(
        pageable: Pageable,
        order: String?,
        type: SearchType?,
        keyword: String?,
        status: PlanStatus?
    ): Page<Plan>
}