package com.taekyoung.planner.domain.plan.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.*
import com.querydsl.jpa.impl.JPAQuery
import com.taekyoung.planner.domain.plan.model.Plan
import com.taekyoung.planner.domain.plan.model.PlanStatus
import com.taekyoung.planner.domain.plan.model.QPlan
import com.taekyoung.planner.domain.plan.model.SearchType
import com.taekyoung.planner.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PlanRepositoryImpl : QueryDslSupport(), CustomPlanRepository {
    private val plan = QPlan.plan
    override fun findByAll(
        pageable: Pageable,
        order: String?,
        type: SearchType?,
        keyword: String?,
        status: PlanStatus?
    ): Page<Plan> {

        println("\\\\\\\\\\${pageable.sort}")

        val sort = getOrder(order)

        val totalCount = queryFactory
            .select(plan.count()).from(plan)
            .fetchOne() ?: 0L

        val result = queryFactory
            .selectFrom(plan).where(deleteAtIsNull(), search(type, keyword), status(status))
            .orderBy(sort)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong()).fetch()

        return PageImpl(result, pageable, totalCount)


//        return paging(pageable, plan) {
//            queryFactory.selectFrom(plan)
//                .where(deleteAtIsNull(), search(type, keyword), status(status))
////                .orderBy(plan.createdAt.desc())
//        }
    }

    fun getOrder(order: String?): OrderSpecifier<*> {
        return when (order) {
            "writer.asc" -> plan.member.name.asc()
            "writer.desc" -> plan.member.name.desc()
            "createAt.asc" -> plan.createdAt.asc()
            "createAt.desc" -> plan.createdAt.desc()
            "status.desc" -> plan.status.desc()
            else -> throw IllegalArgumentException("ColumnSorted '$order' is unknown")
        }
    }

    fun planPage(pageable: Pageable): Page<Plan> {
        val query =
            queryFactory.selectFrom(plan).where(deleteAtIsNull()).offset(pageable.offset)
                .limit(pageable.pageSize.toLong()).fetch()
        val total = queryFactory.select(plan.count()).where(deleteAtIsNull()).fetchOne() ?: 0L
        return PageImpl(query, pageable, total)
    }

    private fun deleteAtIsNull(): BooleanExpression {
        return plan.deletedAt.isNull()
    }

    private fun titleContains(keyword: String?): BooleanExpression? {
        return keyword?.let { plan.title.contains(it) }
    }

    private fun contentContains(keyword: String?): BooleanExpression? {
        return keyword?.let { plan.content.contains(it) }
    }

    private fun writerEq(keyword: String?): BooleanExpression? {
        return keyword?.let { plan.member.name.eq(it) }
    }

    private fun status(status: PlanStatus?): BooleanExpression? {
        return status?.let { plan.status.eq(it) }
    }

    private fun search(searchType: SearchType?, keyword: String?): BooleanExpression? {
        return when (searchType) {
            SearchType.TITLE -> titleContains(keyword)
            SearchType.CONTENT -> contentContains(keyword)
            SearchType.WRITER -> writerEq(keyword)
            else -> null
        }
    }

}

fun <T> paging(pageable: Pageable, path: EntityPathBase<T>, baseQueryFunc: () -> JPAQuery<T>): Page<T> {
    val baseQuery = baseQueryFunc()

    val totalCount = baseQuery
        .select(path.count())
        .fetchOne() ?: 0L

    val result = baseQuery
        .select(path)
        .offset(pageable.offset)
        .limit(pageable.pageSize.toLong())
        .fetch()
    return PageImpl(result, pageable, totalCount)
}