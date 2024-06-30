package com.taekyoung.planner.domain.plan.repository

import com.taekyoung.planner.domain.common.QueryDslConfig
import com.taekyoung.planner.domain.member.model.Member
import com.taekyoung.planner.domain.member.model.MemberRole
import com.taekyoung.planner.domain.member.repository.MemberRepository
import com.taekyoung.planner.domain.plan.dto.PlanRequest
import com.taekyoung.planner.domain.plan.model.Plan
import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.model.SearchType
import com.taekyoung.planner.domain.plan.repository.PlanRepository
import com.taekyoung.planner.infra.jpa.JPAConfig
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

//@ContextConfiguration(classes = [PlannerApplication::class])
@DataJpaTest
//@Transactional
//@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslConfig::class, JPAConfig::class])
@ActiveProfiles("test")
class PlanRepositoryTest @Autowired constructor(
    private val planRepository: PlanRepository,
    private val membersRepository: MemberRepository,
) {

    @Test
    fun `SearchType과 status가 null 일 경우 전체 데이터 조회되는지 확인`() {
        // GIVEN
        membersRepository.saveAndFlush(MEMBER)
        planRepository.saveAllAndFlush(DEFAULT_PLAN_LIST)

        // WHEN
        val result1 = planRepository.findByAll(PageRequest.of(0, 10), null, null, null, null)
        val result2 = planRepository.findByAll(PageRequest.of(0, 8), null, null, null, null)
        val result3 = planRepository.findByAll(PageRequest.of(0, 5), null, null, null, null)

        // THEN
        result1.content.size shouldBe 10
        result2.content.size shouldBe 8
        result3.content.size shouldBe 5
    }

    @Test
    fun `SearchType 이 null 이 아닌 경우 Keyword가 포함된것들이 검색되는지 결과 확인`() {
        // GIVEN
        membersRepository.saveAndFlush(MEMBER)
        planRepository.saveAllAndFlush(DEFAULT_PLAN_LIST)

        // WHEN
        val result = planRepository.findByAll(PageRequest.of(0, 10), null, SearchType.TITLE, "test1", null)

        // THEN
        result.content.size shouldBe 3
    }

    @Test
    fun `Keyword 에 의해 조회된 결과가 0건일 경우 결과 확인`() {
        // GIVEN
        membersRepository.saveAndFlush(MEMBER)
        planRepository.saveAllAndFlush(DEFAULT_PLAN_LIST)

        // WHEN
        val result = planRepository.findByAll(PageRequest.of(0, 10), null, SearchType.TITLE, "notting", null)

        // THEN
        result.content.size shouldBe 0
    }

    @Test
    fun `조회된 결과가 11개, PageSize 6일 때 0Page 결과 확인`() {
        // GIVEN
        membersRepository.saveAndFlush(MEMBER)
        planRepository.saveAllAndFlush(DEFAULT_PLAN_LIST)

        // WHEN
        val result = planRepository.findByAll(PageRequest.of(0, 6), null, null, null, null)

        // THEN
        result.content.size shouldBe 6
        result.isLast shouldBe false
        result.totalPages shouldBe 2
        result.number shouldBe 0
        result.totalElements shouldBe 11
    }

    @Test
    fun `조회된 결과가 11개, PageSize 6일 때 1Page 결과 확인`() {
        // GIVEN
        membersRepository.saveAndFlush(MEMBER)
        planRepository.saveAllAndFlush(DEFAULT_PLAN_LIST)

        // WHEN
        val result = planRepository.findByAll(PageRequest.of(1, 6), null, null, null, null)

        // THEN
        result.content.size shouldBe 5
        result.isLast shouldBe true
        result.totalPages shouldBe 2
        result.number shouldBe 1
        result.totalElements shouldBe 11
    }

    @Test
    fun `상태가 완료인것들 조회시 완료인것들만 나오는지 결과 확인`() {
        // GIVEN
        membersRepository.saveAndFlush(MEMBER)
        planRepository.saveAllAndFlush(DEFAULT_PLAN_LIST)

        // WHEN
        val result = planRepository.findByAll(PageRequest.of(0, 100), null, null, null, PlanStatus.COMPLETED)

        // THEN
        result.content.size shouldBe 2
        result.content.forEach { it.status shouldBe PlanStatus.COMPLETED }
    }

    companion object {

        private val MEMBER = Member("a@a", "test_member", "test1234", MemberRole.HOST, null, null)
        private val DEFAULT_PLAN_LIST = listOf(
            Plan("test1", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test2", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test3", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test4", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test5", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test6", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test7", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test8", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test9", "content", MEMBER, PlanStatus.COMPLETED, LocalDateTime.now(), null, null),
            Plan("test10", "content", MEMBER, PlanStatus.READY, LocalDateTime.now(), null, null),
            Plan("test11", "content", MEMBER, PlanStatus.COMPLETED, LocalDateTime.now(), null, null),
        )
    }


//    @Test
//    fun `dynamicInsertTest`() {
//        // given
//        val newPlan = PlanRequest(title = "New Plan", content = "new Plan content")
//
//        // when
////        val member = memberRepository.findByIdAndDeletedAtIsNull(1L)
//        val member = Member("a@a", "test", "test1234", MemberRole.HOST, null, null)
//        val savedPlan = planRepository.save(Plan.of(newPlan.title, newPlan.content, member))
//
//        // then
//        assertThat(savedPlan).isNotNull
//    }

}
