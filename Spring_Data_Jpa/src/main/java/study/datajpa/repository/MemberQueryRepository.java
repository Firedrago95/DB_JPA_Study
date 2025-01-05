package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;

/**
 * 꼭 커스텀 인터페이스 구현체를 만들지 않고, 별도의 리포지토리 빈을 사용해도 된다.
 * 핵심 비즈니스 로직, 화면단인지, 등에따라 클래스를 잘 분리해보자
 */
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findMemberQueryDsl() {
        // 복잡한 QueryDSL 이 온다고 가정하고 보자
        return em.createQuery("select m from Membr m")
                .getResultList();
    }
}
