package hello.jpa.jpql;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA  = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB  = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원2");
            member3.setTeam(teamB);
            em.persist(member3);

            // 영속성 컨텍스트 무시, DB 직접 쿼리 -> 데이터 정합성 문제 (객체와 DB)
            // Flush 자동 호출
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            // 벌크연산을 먼저실행 또는 벌크연산 수행후 컨텍스트 초기화
            em.clear();

            tx.commit();
        } catch (Exception e) {
            // 문제 있으면 롤백
            tx.rollback();
        } finally {
            // EntityManager 닫기 , 작업마다 생성
            em.close();
        }
        // EntityManagerFactory 하나만 유지
        emf.close();

    }
}
