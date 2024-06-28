package com.taekyoung.planner.domain.plan.controller

import com.taekyoung.planner.domain.plan.comment.dto.CommentResponse
import com.taekyoung.planner.domain.plan.dto.PlanDetailResponse
import com.taekyoung.planner.domain.plan.dto.PlanRequest
import com.taekyoung.planner.domain.plan.dto.PlanResponse
import com.taekyoung.planner.domain.plan.dto.UpdatePlanRequest
import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.model.SearchType
import com.taekyoung.planner.domain.plan.service.PlanService
import com.taekyoung.planner.infra.security.MemberPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/plans")
@RestController
class PlanController(
    private val planService: PlanService,
//    private val preA
) {

    @GetMapping()
    fun getPlanAll(
        @PageableDefault(
            page = 0,
            size = 10
        ) pageable: Pageable,
        @RequestParam order: String?,
        @RequestParam type: SearchType?,
        @RequestParam keyword: String?,
        @RequestParam status: PlanStatus?
    ): ResponseEntity<Page<PlanResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getPlanAll(pageable, order, type, keyword, status))
    }

    @GetMapping("/{planId}")
    fun getPlan(@PathVariable planId: Long): ResponseEntity<PlanDetailResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getPlanById(planId))
    }

    @PostMapping()
    fun createPlan(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestBody request: PlanRequest
    ): ResponseEntity<PlanResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(request, principal.id))
    }

    @PutMapping("/{planId}")
    fun updatePlan(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @RequestBody request: UpdatePlanRequest,
        @PathVariable planId: Long
    ): ResponseEntity<PlanResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(planService.updatePlan(planId, principal.id, request))
    }

    @DeleteMapping("/{planId}")
    fun deletePlan(
        @AuthenticationPrincipal principal: MemberPrincipal,
        @PathVariable planId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(planService.deletePlan(planId, principal.id))
    }

}