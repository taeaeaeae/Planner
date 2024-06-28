package com.taekyoung.planner.domain.plan.comment.repository

import com.taekyoung.planner.domain.plan.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByPlanIdAndDeletedAtIsNull(planId: Long): List<Comment>
    fun findByIdAndDeletedAtIsNull(id: Long): Comment?
}