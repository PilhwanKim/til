package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.Inheritance.Book;
import dev.leonkim.hellojpa.domain.Inheritance.Item;
import dev.leonkim.hellojpa.domain.Member;
import dev.leonkim.hellojpa.domain.Team;
import dev.leonkim.hellojpa.domain.value.Address;
import dev.leonkim.hellojpa.dto.MemberDTO;
import org.hibernate.dialect.MySQL57Dialect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.List;

class JPQLBasicTest {
    /**
     * Criteria -> 너무 복잡하고 실용성이 없다.
     * QueryDSL -> 자바코드로 동적 쿼리 작성, JPQL 빌더 역할, 실무 사용 권장
     * 결론 : Criteria 대신 QueryDSL 을 쓰자.
     * NativeQuery -> 실제 SQL 문법을 씀
     * SpringJdbcTemplate
     */

    @Test
    @DisplayName("JPQL 기본 예제")
     void basicTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member saveMember = new Member();
            saveMember.setName("kim pilhwan");
            em.persist(saveMember);

            List<Member> members = em.createQuery("select m From bMember m where m.username like '%kim%'",
                    Member.class
            ).getResultList();

            for (Member member : members) {
                System.out.println("member1 = " + member.getName());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("TypedQuery & Parameter 바인딩")
    void typedQueryAndParameterTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member saveMember = new Member();
            saveMember.setName("member1");
            saveMember.setAge(10);
            em.persist(saveMember);

            // 파라메터 바인딩은 무조건 - 이름기준으로(위치기준 X)
            List<Member> members = em
                    .createQuery("select m From bMember m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getResultList();

            System.out.println("members = " + members);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    /**
     * 프로젝션 - SELECT 절에 조회 대상을 지정하는 것
     * - 엔티티
     *      - SELECT m FROM Member m
     *      - SELECT m.team FROM Member m
     * - 임베디드 타입
     *      - SELECT m.address FROM Member m
     * - 스칼라 타입(숫자, 문자, 기본 데이터 타입)
     *      - SELECT m.username, m.age FROM Member m
     *
     */
    @Test
    @DisplayName("프로젝션")
    void projectionTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member saveMember = new Member();
            saveMember.setName("member1");
            saveMember.setAge(10);
            em.persist(saveMember);

            em.flush();
            em.clear();

            // 이 콜렉션 내 Member 객체들은 영속성 컨텍스트에 다 들어갈까?
            // 정답은 Yes! (Entity 프로젝션일 경우에만 영속성 컨텍스트에 들어감)
            List<Member> members = em.createQuery("select m From bMember m where m.username like '%kim%'",
                    Member.class
            ).getResultList();

            // 영속성 컨텍스트 이므로 꺼내서 dirty checking 에 의한 update 가능
            Member findMember = members.get(0);
            findMember.setAge(20);

            // 임베디드 타입 프로젝션
            List<Address> addresses = em.createQuery("select o.address from bOrder o", Address.class).getResultList();

            // 스칼라 타입 프로젝션 방식 1
            List<Object[]> resultList = em.createQuery("select m.username, m.age from bMember m")
                    .getResultList();

            Object[] result = resultList.get(0);
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            // 스칼라 타입 프로젝션 방식 2
            List<MemberDTO> memberDTOS = em.createQuery(
                    "select new dev.leonkim.hellojpa.dto.MemberDTO(m.username, m.age) from bMember m",
                    MemberDTO.class
            ).getResultList();
            MemberDTO memberDTO = memberDTOS.get(0);
            System.out.println("memberDTO.username = " + memberDTO.getUsername());
            System.out.println("memberDTO.age = " + memberDTO.getAge());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("페이징 - 각 DB의 방언에 따라 SQL 번역됨")
    void pagingTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for (int i = 0; i < 100; i++) {
                Member saveMember = new Member();
                saveMember.setName("member" + i);
                saveMember.setAge(i);
                em.persist(saveMember);
            }

            em.flush();
            em.clear();

            List<Member> members = em.createQuery("select m from bMember m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("members.size() = " + members.size());
            for (Member member : members) {
                System.out.println("member = " + member);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("조인")
    void joinTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member saveMember = new Member();
            saveMember.setName("member");
            saveMember.setAge(10);
            saveMember.changeTeam(team);
            em.persist(saveMember);

            em.flush();
            em.clear();

            // inner join
            List<Member> members = em.createQuery("select m from bMember m inner join m.team t", Member.class)
                    .getResultList();

            // left outer join
//            List<Member> members = em.createQuery("select m from bMember m left join m.team t", Member.class)
//                    .getResultList();

            // 조인 대상 필터링(On 절)
//            List<Member> members = em.createQuery("select m from bMember m left join m.team t on t.name = 'teamA'", Member.class)
//                    .getResultList();

            System.out.println("members.size() = " + members.size());
            for (Member member : members) {
                System.out.println("member = " + member);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("서브쿼리")
    void subQueryTest() {
        /**
         * 서브쿼리 지원 함수
         * - [NOT] EXISTS (subquery): 서브쿼리에 결과가 존재하면 참
         * - {ALL | ANY | SOME} (subquery)
         *      - ALL: 모두 만족하면 참
         *      - ANY, SOME: 같은 의미, 조건을 하나라도 만족하면 참
         * - [NOT] IN (subquery): 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
         *
         * JPA 서브 쿼리 한계
         * - WHERE, HAVING 절에서만 서브 쿼리 사용(JPA 표준)
         * - SELECT 절 서브쿼리는 하이버네이트에서만
         * - FROM 절의 서브쿼리 JPQL 에선 불가능
         *      - JOIN 절로 풀어서 해결
         *      - 에플리케이션에서 조립해서 풀기
         *      - 쿼리 분해해서 2번 날리기
         *
         * 나이가 평균보다 많은 회원
         * select m from Member m
         * where m.age > (select avg(m2.age) from Member m2;
         *
         * 한 건이라도 주문한 고객
         * select m from Member m
         * where (select count(o) from Order o where m = o.member) > 0;
         */
    }

    @Test
    @DisplayName("JPQL - type() 상속관계 엔티티에서 사용")
    void jpqlTypeTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");

            em.persist(book);

            List<Item> resultList = em.createQuery("select i from bItem i where type(i) = bBook ", Item.class)
                    .getResultList();
            for (Item item : resultList) {
                System.out.println("item = " + item);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("JPQL - CASE-WHEN")
    void jpqlCaseTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member saveMember = new Member();
            saveMember.setName("member");
            saveMember.setAge(10);
            saveMember.changeTeam(team);
            em.persist(saveMember);

            em.flush();
            em.clear();

            String query =
                    "select " +
                            "case when m.age <= 10 then '학생요금' " +
                            "     when m.age >= 60 then '경로요금' " +
                            "     else '일반요금' " +
                            "end "  +
                    "from bMember m";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("JPQL - Coalesce")
    void jpqlCoalesceTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member saveMember = new Member();
            saveMember.setName("member");
            saveMember.setAge(10);
            saveMember.changeTeam(team);
            em.persist(saveMember);

            em.flush();
            em.clear();

            // coalesce 함수
            String query = "select coalesce(m.username, '이름 없는 회원') as username " +
                            "from bMember m";
            // nullif 함수
//            String query = "select nullif(m.username, '관리자') as username " +
//                    "from bMember m";

            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("JPQL 기본/정의 함수")
    void jpqlBasicFunctionTest() {
        /**
         * JPQL 기본 함수
         * - CONCAT
         * - SUBSTRING
         * - TRIM
         * - LOWER / UPPER
         * - LENGTH
         * - LOCATE(문자의 위치 찾기)
         * - ABS, SQRT, MOD
         * - SIZE, INDEX(JPA 용도)
         *
         * 사용자 정의 함수(Dialect 상속)
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member saveMember = new Member();
            saveMember.setName("member");
            saveMember.setAge(10);
            saveMember.changeTeam(team);
            em.persist(saveMember);

            em.flush();
            em.clear();

            // coalesce 함수
            String query = "select size(t.members) as username from Team t";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }


    @Test
    @DisplayName("JPQL 사용자 정의 함수")
    void jpqlCustomFunctionTest() {
        /**
         * JPQL 기본 함수
         * - CONCAT
         * - SUBSTRING
         * - TRIM
         * - LOWER / UPPER
         * - LENGTH
         * - LOCATE(문자의 위치 찾기)
         * - ABS, SQRT, MOD
         * - SIZE, INDEX(JPA 용도)
         *
         * 사용자 정의 함수(Dialect 상속)
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setName("관리자1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

            // 사용자 정의 함수
            String query = "select function('group_concat', m.username) from bMember m";

            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
