package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;


public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageLoe(condition.getAgeLoe()),
                        ageGoe(condition.getAgeGoe()))
                .fetch();
    }

    /**
     * 단순한 페이징
     */
    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageLoe(condition.getAgeLoe()),
                        ageGoe(condition.getAgeGoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
    
    /**
     * 복잡한 페이징
     * 데이터 조회 쿼리와, 전체 카운트 쿼리를 분리
     */
     @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {

         List<MemberTeamDto> content = queryFactory
                 .select(new QMemberTeamDto(
                         member.id,
                         member.username,
                         member.age,
                         team.id,
                         team.name))
                 .from(member)
                 .leftJoin(member.team, team)
                 .where(
                         usernameEq(condition.getUsername()),
                         teamNameEq(condition.getTeamName()),
                         ageLoe(condition.getAgeLoe()),
                         ageGoe(condition.getAgeGoe()))
                 .offset(pageable.getOffset())
                 .limit(pageable.getPageSize())
                 .fetch();

         long total = queryFactory
                 .select(member)
                 .from(member)
                 .leftJoin(member.team, team)
                 .where(
                         usernameEq(condition.getUsername()),
                         teamNameEq(condition.getTeamName()),
                         ageLoe(condition.getAgeLoe()),
                         ageGoe(condition.getAgeGoe()))
                 .fetchCount();

         return new PageImpl<>(content, pageable, total);
     }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? member.team.name.eq(teamName) : null;
    }

    private BooleanExpression ageLoe(Integer ageCond) {
        return ageCond != null ? member.age.loe(ageCond) : null;
    }

    private BooleanExpression ageGoe(Integer ageCond) {
        return ageCond != null ? member.age.goe(ageCond) : null;
    }

}
