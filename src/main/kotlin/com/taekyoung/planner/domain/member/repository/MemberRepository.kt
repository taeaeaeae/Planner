package com.taekyoung.planner.domain.member.repository

import com.taekyoung.planner.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository


interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Member?
    fun findByIdAndDeletedAtIsNull(memberId: Long): Member?
}