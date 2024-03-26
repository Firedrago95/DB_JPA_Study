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

            // 비영속
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");

            // 영속
            em.persist(member);

            // 1차 캐시에서 조회
            Member findMember = em.find(Member.class, 101L);
            System.out.println("findMember.id = " + findMember.getName());
            System.out.println("findMember.name = " + findMember.getName());

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
