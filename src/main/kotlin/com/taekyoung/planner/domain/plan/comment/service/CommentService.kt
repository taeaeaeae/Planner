package com.taekyoung.planner.domain.plan.comment.service

import com.taekyoung.planner.domain.plan.comment.dto.CommentRequest
import com.taekyoung.planner.domain.plan.comment.dto.CommentResponse

interface CommentService {

    fun getCommentById(commentId: Long): CommentResponse
    fun createComment(request: CommentRequest, memberId: Long, planId: Long): CommentResponse
    fun updateComment(commentId: Long, memberId: Long, request: CommentRequest): CommentResponse
    fun deleteComment(commentId: Long, memberId: Long)
}