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
            Team team  = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            // From 절에서 명시적 조인으로 별칭을 얻으면 별칭으로 탐색가능
            String query = "select m From Team t join t.members m";

            List<Collection> result = em.createQuery(query, Collection.class)
                    .getResultList();

            System.out.println("result = " + result);

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
