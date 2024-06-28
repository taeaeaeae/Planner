package com.taekyoung.planner.domain.plan.service

import com.taekyoung.planner.domain.plan.comment.dto.toResponse
import com.taekyoung.planner.domain.plan.comment.repository.CommentRepository
import com.taekyoung.planner.domain.exception.ModelNotFoundException
import com.taekyoung.planner.domain.member.repository.MemberRepository
import com.taekyoung.planner.domain.plan.dto.*
import com.taekyoung.planner.domain.plan.model.Plan
import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.model.SearchType
import com.taekyoung.planner.domain.plan.repository.PlanRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlanService(
    private val planRepository: PlanRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository
) {

    fun getPlanAll(
        pageable: Pageable,
        order: String?,
        type: SearchType?,
        keyword: String?,
        status: PlanStatus?
    ): Page<PlanResponse> {
        return planRepository.findByAll(pageable, order, type, keyword, status).map { it.toResponse() }
    }

    fun getPlanById(planId: Long): PlanDetailResponse {
        val plan = planRepository.findByIdAndDeletedAtIsNull(planId) ?: throw ModelNotFoundException("plan", planId)
        val comment = commentRepository.findByPlanIdAndDeletedAtIsNull(planId)
        return PlanDetailResponse(plan.toResponse(), comment.map { it.toResponse() })
    }

    @Transactional
    fun createPlan(request: PlanRequest, memberId: Long): PlanResponse {
        val writer = memberRepository.findByIdAndDeletedAtIsNull(memberId)
            ?: throw ModelNotFoundException("member", memberId)
        val plan = Plan.of(
            title = request.title,
            content = request.content,
            member = writer
        )
        return planRepository.save(plan).toResponse()
    }

    @Transactional
    fun updatePlan(planId: Long, memberId: Long, request: UpdatePlanRequest): PlanResponse {
        val plan = planRepository.findByIdAndDeletedAtIsNull(planId) ?: throw ModelNotFoundException("plan", planId)
        if (plan.member.id != memberId) throw AccessDeniedException("You are not have this plan")
        plan.updatePlan(title = request.title, content = request.content, status = request.status)
        return plan.toResponse()
    }

    @Transactional
    fun deletePlan(planId: Long, memberId: Long) {
        val plan = planRepository.findByIdAndDeletedAtIsNull(planId) ?: throw ModelNotFoundException("plan", planId)
        if (plan.member.id != memberId) throw AccessDeniedException("You are not have this plan")
        plan.deletePlan()
    }


}