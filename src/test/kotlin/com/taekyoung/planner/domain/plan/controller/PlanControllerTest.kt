package com.taekyoung.planner.domain.plan.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.taekyoung.planner.domain.member.model.MemberRole
import com.taekyoung.planner.domain.plan.dto.PlanDetailResponse
import com.taekyoung.planner.domain.plan.dto.PlanRequest
import com.taekyoung.planner.domain.plan.dto.PlanResponse
import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.service.PlanServiceImpl
import com.taekyoung.planner.infra.security.MemberPrincipal
import com.taekyoung.planner.infra.security.jwt.TokenProvider
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class PlanControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val tokenProvider: TokenProvider,
    private val objectMapper: ObjectMapper = jacksonObjectMapper().registerKotlinModule()
        .registerModule(JavaTimeModule()),
) : DescribeSpec({
    extensions(SpringExtension)

    afterContainer { clearAllMocks() }


    val planService = mockk<PlanServiceImpl>()

    describe("GET /plans/{planId}는") {
        context("존재하는 id 보낼 때") {
            it("200을 보내야함") {
                val planId = 1L

                every { planService.getPlanById(any()) } returns PlanDetailResponse(
                    plan = PlanResponse(
                        planId = planId,
                        title = "test_title",
                        content = "abc",
                        status = PlanStatus.READY,
                        writer = "나",
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    ),
                    comment = emptyList()
                )

                val token = tokenProvider.generateAccessToken(
                    subject = "1",
                    email = "test@gmail.com",
                    role = MemberRole.HOST
                )

                val result = mockMvc.perform(
                    get("/plans/$planId")
                        .header("Authorization", "Bearer $token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 200

                val responseDto = objectMapper.readValue(
                    result.response.contentAsString,
                    PlanDetailResponse::class.java
                )
                responseDto.plan.planId shouldBe planId
            }

        }
    }

    describe("POST /plans는") {
        context("정상값을보낼때") {
            it("201을 보내야함") {
                val request = PlanRequest(
                    title = "test_title",
                    content = "abc"
                )
                val token = tokenProvider.generateAccessToken(
                    subject = "1",
                    email = "test@gmail.com",
                    role = MemberRole.HOST
                )

                val principal = MemberPrincipal(1, "test@gmail.com", roles = setOf("ROLE_HOST"))

                every { planService.createPlan(request, principal.id) } returns PlanResponse(
                    planId = 1L,
                    title = "test_title",
                    content = "abc",
                    status = PlanStatus.READY,
                    writer = "test",
                    createdAt = LocalDateTime.now(),
                    updatedAt = null
                )

                val result = mockMvc.perform(
                    post("/plans")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $token")
                        .content(jacksonObjectMapper().writeValueAsString(request))
                ).andReturn()

                result.response.status shouldBe 201

                val responseDto = objectMapper.readValue(
                    result.response.contentAsString,
                    PlanResponse::class.java
                )
                responseDto.status shouldBe PlanStatus.READY

            }


        }


    }

}) {
//    @Autowired
//    objectMapper = jacksonObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
}