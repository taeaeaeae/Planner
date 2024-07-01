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
