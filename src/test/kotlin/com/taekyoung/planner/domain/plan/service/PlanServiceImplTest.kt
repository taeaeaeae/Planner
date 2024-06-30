package com.taekyoung.planner.domain.plan.service

import com.taekyoung.planner.domain.exception.ModelNotFoundException
import com.taekyoung.planner.domain.member.repository.MemberRepository
import com.taekyoung.planner.domain.plan.comment.repository.CommentRepository
import com.taekyoung.planner.domain.plan.repository.PlanRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
@ExtendWith(MockKExtension::class)
class PlanServiceImplTest : BehaviorSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }
    val planRepository = mockk<PlanRepository>()
    val commentRepository = mockk<CommentRepository>()
    val memberRepository = mockk<MemberRepository>()

    val planService = PlanServiceImpl(planRepository, commentRepository, memberRepository)

    Given("plan 목록이 존재하지 않을때") {

        When("특정 plan조회를 요청하면") {

            Then("ModelNotFoundException이 발생해야 한다.") {
                val planId = 100L
                every { planRepository.findByIdAndDeletedAtIsNull(any()) } returns null

                shouldThrow<ModelNotFoundException> {
                    planService.getPlanById(planId)
                }
            }

        }
    }
})