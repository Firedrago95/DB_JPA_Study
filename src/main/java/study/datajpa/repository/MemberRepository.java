package study.datajpa.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername") 생략가능 메서드명으로 호출할수 있다.
    List<Member> findByUsername(@Param("username") String username);

//  실무에서는 메소드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 메서드 이름이 매우 지저분해진다.
//  따라서 @Query 기능을 자주 사용하게 된다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /**
     * 페이징 쿼리는 반환타입에 따라 효과가 달라진다.
     *
     * 총 페이지 없이, limit에 한개 추가로 가져옴, 모바일 더보기 환경등에서 사용
     * Slice<Member> findByAge(int age, Pageable pageable);
     *
     * 그냥 페이징 기능 필요없고, 데이터만 받아오고 싶다.
     * List<Member> findByAge(int age, Pageable pageable);
     */

    // 카운트 쿼리 최적화 할 수 있다.
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    // 벌크성 수정 쿼리는 영속성 컨텍스트와 무관하게 동작한다.
    // 영속성 컨텍스트 비워놓거나, 수정 후 영속성 컨텍스트를 비워줘야한다.
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    
    // JPQL로 패치 조인 -> N+1 문제를 해결하기위해 쿼리 한방으로 조회
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // 공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);
    
    // 변경감지 안쓴다. 스냅샷을 안만들기 위한 최적화
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    Member findReadOnlyByUsername(String username);

    List<UsernameOnlyDto> findProjectionsByUsername(@Param("username")String username);
}
