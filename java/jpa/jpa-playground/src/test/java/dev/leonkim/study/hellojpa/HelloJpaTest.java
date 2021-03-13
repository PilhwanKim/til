package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

class HelloJpaTest {
    /**
     * 주의
     * * 엔티티 매니저 팩토리는 하나만 생성헤서 애플리케이션 전체에서 공유
     * * 엔티티 매니저는 쓰레드간에 공유 X
     * * JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
     */

    @Test
    @DisplayName("JPA_생성_코드")
     void JPA_persist() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 도메인 로직
            Member member = new Member();
            member.setName("HelloA");

            em.persist(member);
            // 도메인 로직

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("JPA_조회_코드")
    void JPA_find() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 도메인 로직
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            // 도메인 로직

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("JPQL조회 - 다수개의 오브젝트를 쿼리로 가져올 때")
    void JPQL() {
        /**
         * JPA를 사용하면 엔티티 객체를 중심으로 개발
         * 문제는 검색 쿼리
         * 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
         * 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
         * 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요
         * JPQL은 엔티티 객체를 대상으로 쿼리
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 도메인 로직
            List<Member> findMembers = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(0) // 페이징 offset
                    .setMaxResults(10) // 페이징 limit
                    .getResultList();

            for (Member member : findMembers) {
                System.out.println("member.name = " + member.getName());
            }
            // 도메인 로직

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("JPA_수정_코드")
    void JPA_update() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 도메인 로직
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
            // 도메인 로직

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("JPA_삭제_코드")
    void JPA_remove() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 도메인 로직
            Member findMember = em.find(Member.class, 1L);
            em.remove(findMember);
            // 도메인 로직

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
