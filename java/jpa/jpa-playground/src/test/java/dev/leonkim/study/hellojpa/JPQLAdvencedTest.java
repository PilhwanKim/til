package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.Member;
import dev.leonkim.hellojpa.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

class JPQLAdvencedTest {

    @Test
    @DisplayName("경로 탐색(경로 표현식)")
     void navigatePathExpressionTest() {
        /**
         * 경로 표현식 - .(점)을 찍어 객체 그래프를 탐색하는 것
         * select m.username -> 상태 필드
         *  from Member m
         *      join m.team t -> 단일 값 연관 필드
         *      join m.orders o -> 컬렉션 값 연관 필드
         *  where t.name = '팀A'
         *
         *  상태 필드 - 단순한 값 저장 필드
         *      - 경로 탐색의 끝, 이후의 탐색 없음
         *  연관 필드 - 연관관계를 위한 필드
         *      단일 값 연관 필드 - 대상이 엔티티
         *          - 묵시적 내부 조인 발생, 탐색을 더 할 수 있음
         *      컬렉션 값 연관 필드 - 대상이 컬렉션
         *          - 묵시적 내부 조인 발생, 이후 탐색은 불가능
         *          - 명시적 조인으로 별칭을 통해 이후 탐색 가능
         *
         *  명시적 조인 - FROM 절에서 join 키워드 직접 사용 (별칭을 통해 탐색)
         *  묵시적 조인 - 경로 표현식에 의해 묵시적으로 SQL 조인 발생(내부 조인만 가능)
         *  * 명시적 조인(추천), 묵시적 조인(쓰지 말것)
         *
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team savedTeam = new Team();
            savedTeam.setName("팀1");
            em.persist(savedTeam);

            Member member1 = new Member();
            member1.setName("관리자1");
            member1.setTeam(savedTeam);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("관리자2");
            member2.setTeam(savedTeam);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select m.team From bMember m";
            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : result) {
                System.out.println("team = " + team);
            }

            query = "select t.members From Team t";
            List<Member> result2 = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member : result2) {
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
    @DisplayName("페치 조인(fetch join)")
    void fetchJoinTest() {
        /**
         * 페치 조인
         * - JPQL에서 성능 최적화를 위해 제공하는 기능
         * - 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
         * - join fetch 명령어 사용
         * - JPQL 에서의 eager loading(즉시 로딩) 개념이다.
         * - 객체 그래프를 SQL 한번에 조회하는 개념
         * - JPQL 에서 조인된 연관관계 모두 영속화 한다.
         *
         * 페치 조인과 일반 조인의 차이
         * - JPQL은 결과 반환 때 연관관계 고려 X
         * - SELECT 절에 지정한 엔티티만 조회한다
         * - 예제에서는 Team 만 조회, Member 조회 하지 않음
         *
         * 페치 조인의 특징과 한계
         * - 페치 조인 대상에는 별칭을 줄 수 없다.
         *      - 페치 조인 대상(연관관계)은 필터링 할수 없다.
         *      - ex) select t
         *            from Team t join fetch t.members m
         *            where m.age > 10
         * - 둘 이상의 컬렉션은 페치 조인 할 수 없다.
         *      - 1:n:m 의 데이터 뻥튀기
         * - 페이징 API 를 사용 할 수 없다.
         *      - 1:n 의 데이터 뻥튀기(중복된 row) 때문에 정확하게 페이징 갯수가 정해지지 않음
         *      - 경고 로그를 남기고 메모리에서 페이징 함(매우 위험)
         * - 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
         * - 최적화가 필요한 곳은 페치 조인 적용
         *
         * 정리
         * - 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
         * - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
         *   페치 조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.setTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setName("회원4");
            em.persist(member4);

            em.flush();
            em.clear();

            String query = "select m From bMember m join fetch m.team t";
            // 쿼리를 바꿔서 실행해 보자 (n + 1 문제)
//            String query = "select m From bMember m";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getName() + ", " + member.getTeam().getName());
                // (중요!) 패치조인을 걸지 않으면 loop 돌때마다 team 을 select 한다.
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
    @DisplayName("페치 조인(fetch join) - 컬랙션")
    void fetchJoinCollectionTest() {
        /**
         * 1:n 조인은 n 기준으로 데이터가 뻥튀기 된다. -> 해결책으로 DISTINCT를 쓸수 있다.
         *
         * JPQL 의 DISTINCT 2가지 기능
         * - SQL의 DISTINCT 를 추가
         * - 애플리케이션에서 엔티티 중복 제거
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.changeTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.changeTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.changeTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setName("회원4");
            em.persist(member4);

            em.flush();
            em.clear();

            String query = "select distinct t From Team t join fetch t.members";
            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : result) {
                List<Member> members = team.getMembers();
                System.out.println("team = " + team.getName() + "|members = " + members.size());
                for (Member member : members) {
                    System.out.println("member = " + member);
                }
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
    @DisplayName("페치 조인(fetch join) 문제 회피")
    void batchSizeTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.setTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setName("회원4");
            em.persist(member4);

            em.flush();
            em.clear();

            String query = "select t From Team t";
            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : result) {
                List<Member> members = team.getMembers();
                System.out.println("team = " + team.getName() + "|members = " + members.size());
                for (Member member : members) {
                    System.out.println("member = " + member);
                }
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
    @DisplayName("엔티티 직접 사용 - PK,FK")
    void entityDirectUseTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.setTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setName("회원4");
            em.persist(member4);

            em.flush();
            em.clear();

            // SQL 에서는 Team id 를 비교한다.
            String query = "select m From bMember m where m.team = :team";
            List<Member> members = em.createQuery(query, Member.class)
                    .setParameter("team", team1)
                    .getResultList();

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
    @DisplayName("Named 쿼리")
    void namedQueryTest() {
        /**
         * 네임드 쿼리
         * - JPQL 의 특정 쿼리를 이름 지어 읽어옴
         * - 정적 쿼리
         * - 애플리케이션 로딩 시점에 쿼리를 검증(컴파일 타임)
         * - XML 에서도 정의할 수 있음
         * - Spring Data JPA 에서 @Query 와 같음
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.setTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setName("회원4");
            em.persist(member4);

            em.flush();
            em.clear();

            List<Member> resultList = em.createNamedQuery("bMember.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member member : resultList) {
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
    @DisplayName("벌크 연산")
    void bulkOperateTest() {
        /**
         * 예) 재고가 10개 미만인 모든 상품의 가격을 10% 상승
         *
         * 주의점!
         * - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
         *      - 벌크 연산을 먼저 실행
         *      - 벌크 연산 수행 후 영속성 컨텍스트 초기화(필수!)
         *
         * Spring Data JPA
         * - @Modifying
         * - @Query
         * -> 벌크 연산 수행 후 영속성 컨텍스트 초기화 해주는 기능
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀A");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.setTeam(team1);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.setTeam(team2);
            em.persist(member3);

            Member member4 = new Member();
            member4.setName("회원4");
            em.persist(member4);

            // FLUSH 자동 호출
            int resultCount = em.createQuery("update bMember m set m.age = 20")
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);
            
            // 초기화 하는것과 안하는 것의 차이를 알아보자.
            // 초기화 하면? - 영속성 내에 엔티티 없으므로 db update 된 내용으로 직접 조회된 내용 엔티티에서 읽을수 있음
            // 초기화 안하면? - 영속성 내에 엔티티를 꺼내서 씀, db update 되지 않은 내용 그대로 읽음(정합성 X)
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
