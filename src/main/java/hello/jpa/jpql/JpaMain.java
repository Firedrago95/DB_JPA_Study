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
            
            // 엔티티 프로젝션 -> 영속성 관리 됨
            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();
            // 임베디드 타입 프로젝션 (엔티티로부터 시작해야 함)
            List<Address> embedded = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();
            // 스칼라 타입 프로젝션 (기본 데이터 타입)
            em.createQuery("select distinct m.username from Member m")
                    .getResultList();


            // 1-1 Object[] 로 여러가지 값 조회
            List resultList1 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            Object o = resultList1.get(0);
            Object[] objArray = (Object[]) o;
            System.out.println("username = " + objArray[0]);
            System.out.println("age = " + objArray[1]);

            // 1-2 제네릭 Object[] 로 여러가지 값 조회
            List<Object[]> resultList2 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            // 1-3 new 명령어로 여러가지 값 조회
            List<MemberDTO> resultList3 = em.createQuery("select distinct new jpql.MemberDTO (m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();


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
