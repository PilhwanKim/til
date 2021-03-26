package dev.leonkim.springdatajpa.repository;

import dev.leonkim.springdatajpa.dto.MemberDto;
import dev.leonkim.springdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new dev.leonkim.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String username);

    Optional<Member> findOptionalByUsername(String username);

//  join 된 쿼리를 페이징 하는 경우가 생김, 이럴때는 카운트 쿼리를 분리하여 선언 가능하게 함
//    @Query(value = "select m from Member m left join m.team t",
//            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // JPQL의 패치 조인 하는 방법
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // Spring Data JPA 에서 내부적으로 fetch join 을 하는것과 같다.
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // Spring Data JPA 에서 내부적으로 fetch join 을 하는것과 같다.
    @EntityGraph("Member.all")
    List<Member> findEntityByUsername(@Param("username") String username);

}
