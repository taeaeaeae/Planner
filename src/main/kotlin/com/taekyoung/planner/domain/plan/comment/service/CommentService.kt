package com.taekyoung.planner.domain.plan.comment.service

import com.taekyoung.planner.domain.exception.ModelNotFoundException
import com.taekyoung.planner.domain.member.repository.MemberRepository
import com.taekyoung.planner.domain.plan.comment.dto.CommentRequest
import com.taekyoung.planner.domain.plan.comment.dto.CommentResponse
import com.taekyoung.planner.domain.plan.comment.dto.toResponse
import com.taekyoung.planner.domain.plan.comment.model.Comment
import com.taekyoung.planner.domain.plan.comment.repository.CommentRepository
import com.taekyoung.planner.domain.plan.repository.PlanRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val planRepository: PlanRepository,
) {

    fun getCommentById(commentId: Long): CommentResponse {
        val comment = commentRepository.findByIdAndDeletedAtIsNull(commentId) ?: throw ModelNotFoundException(
            "comment",
            commentId
        )
        return comment.toResponse()
    }

    @Transactional
    fun createComment(request: CommentRequest, memberId: Long, planId: Long): CommentResponse {
        val writer = memberRepository.findByIdAndDeletedAtIsNull(memberId)
            ?: throw ModelNotFoundException("member", memberId)
        val plan = planRepository.findByIdAndDeletedAtIsNull(planId) ?: throw ModelNotFoundException("plan", planId)
        val comment = Comment.of(content = request.content, member = writer, plan = plan)
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    fun updateComment(commentId: Long, memberId: Long, request: CommentRequest): CommentResponse {
        val comment = commentRepository.findByIdAndDeletedAtIsNull(commentId) ?: throw ModelNotFoundException(
            "comment",
            commentId
        )
        if (comment.member.id != memberId) throw AccessDeniedException("You are not have this comment")
        comment.updateComment(content = request.content)
        return comment.toResponse()
    }

    @Transactional
    fun deleteComment(commentId: Long, memberId: Long) {
        val comment = commentRepository.findByIdAndDeletedAtIsNull(commentId) ?: throw ModelNotFoundException(
            "comment",
            commentId
        )
        if (comment.member.id != memberId) throw AccessDeniedException("You are not have this comment")
        comment.deleteComment()
    }
}