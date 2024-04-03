package hello.jpa.jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // getResultList() : 결과없으면 빈 리스트 반환 (null 걱정 x)
            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            // getSingleResult( ) -> 반환값 하나일때 , 결과없거나, 둘 이상이면 예외발생
            Member singleResult = em.createQuery("select m from Member m where m.id = 1", Member.class)
                    .getSingleResult();

            // 파라미터 바인딩
            em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1");

            em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter(1, "member1");

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
