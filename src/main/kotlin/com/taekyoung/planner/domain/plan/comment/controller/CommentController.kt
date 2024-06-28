package com.taekyoung.planner.domain.plan.comment.controller

import com.taekyoung.planner.domain.plan.comment.dto.CommentRequest
import com.taekyoung.planner.domain.plan.comment.dto.CommentResponse
import com.taekyoung.planner.domain.plan.comment.service.CommentService
import com.taekyoung.planner.infra.security.MemberPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/plan")
@RestController
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("/{planId}")
    fun createComment(
        @PathVariable planId: Long,
        @RequestBody request: CommentRequest,
        @AuthenticationPrincipal principal: MemberPrincipal,
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commentService.createComment(request, principal.id, planId))
    }

    @PutMapping("/comment/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody request: CommentRequest,
        @AuthenticationPrincipal principal: MemberPrincipal,
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentId, principal.id, request))
    }

    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal principal: MemberPrincipal
    ): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentId, principal.id))
    }

    @GetMapping("/comment/{commentId}")
    fun getComments(@PathVariable commentId: Long): ResponseEntity<CommentResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentById(commentId))
    }


}