package com.taekyoung.planner.domain.plan.repository

import com.taekyoung.planner.domain.plan.model.Plan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanRepository : JpaRepository<Plan, Long>, CustomPlanRepository {
    fun findByIdAndDeletedAtIsNull(id: Long): Plan?
    fun existsByIdAndDeletedAtIsNull(id: Long): Boolean
}