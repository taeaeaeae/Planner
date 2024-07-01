# Planner 
할 일을 등록하고 상태에 따라 변경할 수 있는 웹 어플리케이션

## 목차

## 주요기능

<details>
<summary> Member </summary><div>
  
- 회원 가입
  -  이메일은 이메일 형식에 맞추어 중복 없이 구성
  -  비밀번호는 최소 4자 이상이며, 영어와 숫자가 모두 들어가야 함
  -  데이터베이스에 비밀번호를 평문으로 저장하는 것이 아닌, 단방향 암호화 알고리즘을 이용하여 암호화 해서 저장
- 로그인
  -  로그인 버튼을 누른 경우 이메일과 비밀번호가 데이터베이스에 등록됐는지 확인
  -  로그인 성공 시, 로그인에 성공한 유저의 정보를 JWT를 활용하여 토큰 발행

  </div></details>


<details>
<summary> Plan </summary><div>
  
- 전체 게시글 목록 조회
    - 제목, 내용, 작성자명, 상태에 따라서 조회
    - 작성 날짜 기준으로 내림차순 정렬
    - 페이징 조회 사용
- 게시글 작성
    - 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능
    - 이미지 업로드 가능
- 게시글 조회
- 게시글 수정
    - 토큰을 검사하여, 해당 사용자가 작성한 게시글만 수정
- 게시글 삭제
    - 토큰을 검사하여, 해당 사용자가 작성한 게시글만 삭제
             
  </div></details>


<details>
<summary> Comment </summary><div>
  
- 댓글 작성
    - 게시글과 연관 관계를 가진 댓글 테이블 추가
    - 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능

  </div></details>


## 개선내용

<details>
<summary>  Controller, Service 패키지 내 클래스 개선 </summary><div>
- Controller Advice 로 예외 공통화 처리
  
  > GlobalExceptionHandler::class에 @RestControllerAdvice 사용하여 예외 공통화
  
- Service 인터페이스와 구현체 분리하여 추상화
  > CRUD 메서드 추상화

</div></details>


<details>
<summary>  CustomException 정의 및 SpringAOP 적용 </summary><div>
- CustomException 정의
  > RuntimeException 사용하여 ModelNotFoundㄷException 정의


</div></details>



<details>
<summary>  QueryDSL 을 사용하여 검색 기능 만들기 </summary><div>

- booleanExpression 이용하여 검색

```kotlin
  .where(deleteAtIsNull(), search(type, keyword), status(status))
```

</div></details>


<details>
<summary>  다양한 조건을 동적 쿼리로 처리 </summary><div>


  
- SearchType 생성하여 type 분류 후 String으로 키워드 입력받아서 검색기능 처리
- status에 따른 결과 따로 추가

```kotlin
    private fun search(searchType: SearchType?, keyword: String?): BooleanExpression? {
        return when (searchType) {
            SearchType.TITLE -> titleContains(keyword)
            SearchType.CONTENT -> contentContains(keyword)
            SearchType.WRITER -> writerEq(keyword)
            else -> null
        }
    }

 ...
    //plan 상태에 따른결과
    private fun status(status: PlanStatus?): BooleanExpression? {
        return status?.let { plan.status.eq(it) }
    }
```

</div></details>


<details>
<summary>  Pageable 을 사용하여 페이징 및 정렬 기능 만들기 </summary><div>
- Pageable 사용하여 Page 생성
  
```kotlin
        val totalCount = queryFactory
            .select(plan.count()).from(plan)
            .fetchOne() ?: 0L

        val result = queryFactory
            .selectFrom(plan).where(deleteAtIsNull(), search(type, keyword), status(status))
            .orderBy(sort)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong()).fetch()

        return PageImpl(result, pageable, totalCount)
```

</div></details>


<details>
<summary> Controller 테스트 코드 작성 </summary><div>
- MockMvc 를 사용해서 Controller 테스트 코드 작성

  <details>
  
  ```kotlin

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
  }) 
  ```
  </details>
</div></details>


<details>
<summary> Service 테스트 코드 작성 </summary><div>
- Mockito 을 사용하여 Service 테스트 코드 작성

  <details>
    
  ```kotlin

  @SpringBootTest
  @ExtendWith(MockKExtension::class)
  class PlanServiceImplTest : BehaviorSpec({
      extension(SpringExtension)
      afterContainer { clearAllMocks() }
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
  ```
  </details>


</div></details>


<details>
<summary> Repository 테스트 코드 작성 </summary><div>
- @DataJpaTest 를 사용해서 Repository 테스트 코드 작성
  
  <details>

  ```kotlin

  @DataJpaTest
  @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
  @Import(value = [QueryDslConfig::class, JPAConfig::class])
  @ActiveProfiles("test")
  class PlanRepositoryTest @Autowired constructor(
      private val planRepository: PlanRepository,
      private val membersRepository: MemberRepository,
  ) {
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
  ```
  
  </details>

</div></details>

<details>
<summary> imageUpload </summary><div>

</details>


## 프로젝트 구조


## API 명세서

method|Status Code|URL|Request Body|Request Param|Response body
--|--|--|--|--|--
GET|200|/plans||?page=$page&size=$size &sort=$sort &type=$searchType &keyword=&keyword &status=&status|[	{		"planId":"Long",     "title":"String"     "content":"String",     "status":"PlanStatus",      "writer":"String",     "createdAt":"LocalDateTime",     "updateAt":"LocalDateTime"	},	... ]
GET|200|/plans/{planId}||| {		"planId":"Long",     "title":"String"     "content":"String",     "status":"PlanStatus",      "writer":"String",     "createdAt":"LocalDateTime",     "updateAt":"LocalDateTime"	}, { "commentId":"Long", "content":"String", "writer":"String", "createdAt":"LocalDateTime", "updateAt":"LocalDateTime" }
POST|201|/plans|{ "title":"String", "content":"String" } || {		"planId":"Long",     "title":"String"     "content":"String",     "status":"PlanStatus",      "writer":"String",     "createdAt":"LocalDateTime",     "updateAt":"LocalDateTime"	}
PUT|200|/plans/{planId}|{ "title":"String", "content":"String", "status":"PlanStatus" }||{		"planId":"Long",     "title":"String"     "content":"String",     "status":"PlanStatus",      "writer":"String",     "createdAt":"LocalDateTime",     "updateAt":"LocalDateTime"	}
DELETE|204|/plans/{planId}|||
GET|200|/plans/comment /{commentId}|||{  "planId":"Long",  "commentId":"Long",  "comment":"String",  "writer":"String",  "createdAt":"LocalDateTime",  "updatedAt":"LocalDateTime" }
POST|201|/plans/{planId}|{"content":"String"}||{  "planId":"Long",  "commentId":"Long",  "comment":"String",  "writer":"String",  "createdAt":"LocalDateTime",  "updatedAt":"LocalDateTime" }
PUT|200|/plans/comment /{commentId}|{"content":"String"}||{  "planId":"Long",  "commentId":"Long",  "comment":"String",  "writer":"String",  "createdAt":"LocalDateTime",  "updatedAt":"LocalDateTime" }
DELETE|204|/plans/comment /{commentId}|||
POST|200|/auth/signup|{  "email":"String",  "password":"String",   "name":"String",  "role": "MemberRole",  "imageUrl": "String" }||{  "memberId":"Long",  "email":"String",  "name":"String",  "imageUrl":"String" }
POST|200|/auth/signin|{  "email":"String",  "password":"String" }||{ "accessToken":"String" } 


## 개발 도구 및 환경

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) 
![Jdk17](https://img.shields.io/badge/jdk17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"/)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
