package dev.leonkim.springdatajpa.repository;

import dev.leonkim.springdatajpa.dto.MemberDto;
import dev.leonkim.springdatajpa.dto.projections.MemberProjection;
import dev.leonkim.springdatajpa.dto.projections.NestedClosedProjections;
import dev.leonkim.springdatajpa.entity.Member;
import dev.leonkim.springdatajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        // 인터페이스만 설정했는데 왜 실행이 가능하지?
        // memberRepository = class com.sun.proxy.$Proxy99 <- 스프링에서 프록시 기술로 repository 를 직접 생성
        System.out.println("memberRepository = " + memberRepository.getClass());

        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        /**
         * 단지 JpaRepository 공통 인터페이스를 상속 받은 것 만으로도
         * 단순 CRUD 기능을 동일하게 사용 가능하다.
         * -> 의미: 순수 JPA 기술을 반복 사용하지 않아도 된다(노가다를 줄인다)
         */
        Member member1 = new Member("member1");
        Member member2 = new Member("member1");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findMember("AAA", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String username : usernameList) {
            System.out.println("username = " + username);
        }
    }

    @Test
    void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        Member findMember = memberRepository.findMemberByUsername("AAA");
        System.out.println("findMember = " + findMember);

        Member nullFindMember = memberRepository.findMemberByUsername("asdfasdf");
        System.out.println("findMember = " + nullFindMember);

        List<Member> findMembers = memberRepository.findByUsername("AAA");
        System.out.println("List<Member> = " + findMembers + " size = " + findMembers.size());

        List<Member> zeroFindMembers = memberRepository.findByUsername("asdfasdf");
        System.out.println("List<Member> = " + zeroFindMembers + " size = " + zeroFindMembers.size());

        Optional<Member> result = memberRepository.findOptionalByUsername("AAA");
        System.out.println("Optional<Member> = " + result);
    }

    @Test
    void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // spring data 는 page 는 0 부터 시작함
        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");

        //when - Page를 반환으로 바꾸면 totalCount 쿼리를 자동으로 처리해준다.
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        // 영속성 컨텍스트안의 엔티티 객체 age 값은 아직 update 되어 있지 않다. 그래서 flush + clear 를 한다.
        // @Modifying(clearAutomatically = true) 가 같은 역할을 한다.
        // em.clear();

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);

        //then
        assertThat(member5.getAge()).isEqualTo(41);
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    void findMemberLazy() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when

        // (lazy loading)
//        List<Member> members = memberRepository.findAll();
        // (eager loading)
        List<Member> members = memberRepository.findMemberEntityGraph();
//        List<Member> members = memberRepository.findMemberFetchJoin();
//        List<Member> members = memberRepository.findEntityByUsername("member1");

        // findAll vs findMemberFetchJoin - N + 1 문제 확인. fetch join(EAGER LOADING) 효과
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            // (lazy loading) findAll() 은 프록시로 들어옴
            // (eager loading) findMemberFetchJoin() 은 Team 객체로 들어옴
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            // (lazy loading) findAll() 은 별개의 team 쿼라 조회
            // (eager loading) findMemberFetchJoin() 은 이미 조회가 끝나있음
            System.out.println("member.team = " + member.getTeam().getName());
        }

        //then
    }

    @Test
    void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        // dirty checking skip - QueryHint 는 JPA 구현체의 특정 기능 사용 가능하게 구멍을 만듦
        em.flush();

        //then
    }

    @Test
    void lock() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //mysql 쿼리 = SELECT FOR UPDATE
        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    void callCustom() {
        //사용자 정의 레포지토리가 실행되는지 확인
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    void projections() {
        //given
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when - UsernameOnly, UsernameOnlyDto, NestedClosedProjections dto로 각기 변경하면서 차이점 보기
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("member1", NestedClosedProjections.class);

        for (NestedClosedProjections nestedClosedProjections : result) {
            System.out.println("nestedClosedProjections = " + nestedClosedProjections);
        }

    }

    @Test
    void nativeQuery() {
        //given
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
    }
}
