package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // 트랜잭션 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 영속
            Member member = em.find(Member.class, 200L);
            member.setName("AAAAA");

            // 준영속 상태로 만들기
            em.detach(member);

            System.out.println("==============");

            // 트랜잭션 커밋
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
