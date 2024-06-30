import com.taekyoung.planner.PlannerApplication
import com.taekyoung.planner.PlannerApplicationTests
import com.taekyoung.planner.domain.member.model.Member
import com.taekyoung.planner.domain.member.model.MemberRole
import com.taekyoung.planner.domain.member.repository.MemberRepository
import com.taekyoung.planner.domain.plan.dto.PlanRequest
import com.taekyoung.planner.domain.plan.model.Plan
import com.taekyoung.planner.domain.plan.repository.PlanRepository
import com.taekyoung.planner.infra.jpa.JPAConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional

@ContextConfiguration(classes = [PlannerApplication::class])
@DataJpaTest
@Transactional
@Import(JPAConfig::class)
@Rollback(value = false)
class PlanRepositoryTest {

    @Autowired
    lateinit var planRepository: PlanRepository
//    lateinit var memberRepository: MemberRepository

    @Test
    fun `dynamicInsertTest`() {
        // given
        val newPlan = PlanRequest(title = "New Plan", content = "new Plan content")

        // when
//        val member = memberRepository.findByIdAndDeletedAtIsNull(1L)
        val member = Member("a@a", "test", "test1234", MemberRole.HOST, null, null)
        val savedPlan = planRepository.save(Plan.of(newPlan.title, newPlan.content, member))

        // then
        assertThat(savedPlan).isNotNull
        assertThat(savedPlan.title).isEqualTo(newPlan.title)
        assertThat(savedPlan.content).isEqualTo(newPlan.content)
    }

}
