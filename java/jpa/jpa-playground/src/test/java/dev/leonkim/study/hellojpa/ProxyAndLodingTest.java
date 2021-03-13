package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.Member;
import dev.leonkim.hellojpa.domain.Team;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

class ProxyAndLodingTest {

    @Test
    @DisplayName("프록시 - 프록시의 동작원리")
     void getReferenceAndProxy() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setName("hello");

            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.getReference(Member.class, member.getId());

            System.out.println("=============");
            // 프록시 객체를 반환 받음 - DB 조회를 미루는 가짝 엔티티 객체 조회
            // (주의)타입 체크시. == 하면 안됨. instanceof 사용
            System.out.println("before findMember = " + findMember.getClass());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("=============");
            System.out.println("findMember.username = " + findMember.getName());
            System.out.println("after findMember = " + findMember.getClass());
            System.out.println("=============");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("프록시 - 프록시 객체가 쓰이면서 타입객체 체크를 잘 해야함")
    void ProxyTypeChecking() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member1 = new Member();
            member1.setName("hello");
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("hello");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());
            // 프록시 객체를 반환 받음 - DB 조회를 미루는 가짝 엔티티 객체 조회
            Member m2 = em.getReference(Member.class, member2.getId());

            // (주의)타입 체크시. == 하면 안됨. instanceof 사용
            System.out.println("m1 == m2: " + (m1.getClass() == m2.getClass()));
            System.out.println("m1 class: " + (m1 instanceof Member));
            System.out.println("m2 class: " + (m2 instanceof Member));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("프록시 - 실제 엔티티와 프록시 엔티티가 동작하는 원리")
    void createdRealAndProxyEntity() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member1 = new Member();
            member1.setName("hello");
            em.persist(member1);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass());

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember.getClass());

            System.out.println("refMember == findMember: " + (refMember == findMember));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("프록시 - 준영속 상태일때 프록시를 초기화하면 문제 발생(LazyInitializationException)")
    void proxyWithPersistenceContext() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setName("hello");
            em.persist(member1);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass());

            em.detach(refMember);
//            em.clear();
//            em.close();

            refMember.getName();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("프록시 확인 유틸")
    void proxyUtils() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setName("hello");
            em.persist(member1);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass());

            System.out.println("isLoaded : " + emf.getPersistenceUnitUtil().isLoaded(refMember));
            Hibernate.initialize(refMember);
            System.out.println("isLoaded : " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("지연로딩 - 프록시에서 team 을 가져옴")
    void lazyLoading() {
        /**
         * 프록시 사용 주의점
         * - 가급적 지연 로딩만 사용(특히 실무에서)
         * - 즉시 로딩을 적용하면 예상하지 못한 SQL 발생
         *      - (EAGER) 10개의 연관관계가 있다면??? -> join 10개 테이블이 적용!
         * - 즉시 로딩은 JPQL 에서 N+1 문제를 일으킨다.
         *      -
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setName("member1");
            member1.setTeam(team);
            em.persist(member1);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member1.getId());
            // team 이 프록시임을 확인
            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());

            System.out.println("==========");
            // 실제 team 프록시 객체의 초기화 - DB 조회
            m.getTeam().getName();
            System.out.println("==========");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("즉시로딩의 문제 - N + 1")
    void nPlusOne() {
        /**
         * 프록시 사용 주의점
         * - 가급적 지연 로딩만 사용(특히 실무에서)
         * - 즉시 로딩을 적용하면 예상하지 못한 SQL 발생
         *      - (EAGER) 10개의 연관관계가 있다면??? -> join 10개 테이블이 적용!
         * - 즉시 로딩은 JPQL 에서 N+1 문제를 일으킨다.
         * - @XToOne 은 기본이 즉시 로딩
         * - @XToMany 은 기본이 지연 로딩
         * - (즉시로딩의 대안으로) JPQL 의 fetch join, 엔티티 그래프 기능 사용
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setName("member1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
            member2.setTeam(teamB);
            em.persist(member2);

            em.flush();
            em.clear();

            List<Member> members = em.createQuery("select m from bMember m", Member.class)
                    .getResultList();

            // - EAGER 일 경우(N + 1)
            //SQL: select * from Member;
            //SQL: select * from Team where TEAM_ID = xxx
            //SQL: select * from Team where TEAM_ID = yyy

            // - LAZY 일 경우
            //SQL: select * from Member;

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

}
