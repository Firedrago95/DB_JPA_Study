package hellojpa;

import hellojpa.relational.Member;
import hellojpa.relational.Team;
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

            // 팀 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            // JPA가 알아서 FK 값으로 사용
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println(member);
            }

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
