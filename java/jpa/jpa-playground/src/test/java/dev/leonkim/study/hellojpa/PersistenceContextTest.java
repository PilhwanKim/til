package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistenceContextTest {

    /**
     * 영속성 컨텍스트의 이점
     * - 1차 캐시 (Map<@id, @Entity> 타입)
     * - 동일성(identity) 보장
     * - 트랜젝션을 지원하는 쓰기 지연(transactional write-behind)
     * - 변경 감지(Dirty Checking)
     * - 지연 로딩(Lazy Loading)
     */

    @Test
    @DisplayName("비영속-영속 상태")
    void persist() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 비영속 상태
            Member member = new Member();
//            member.setId(100L);
            member.setName("HelloJPA");

            // 영속 상태
            System.out.println("=== BEFORE ===");
            em.persist(member);
            System.out.println("=== AFTER ===");

            // 조회 쿼리가 날라가지 않음. 1차 캐시에서 가져옴
            Member findMember = em.find(Member.class, 100L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

            // insert 쿼리는 commit 직전에 나간다.
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("1차 캐시 - 동일성 보장")
    void findTwice() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 1차 캐시에 없어서 조회쿼리 날림.
            Member findMember1 = em.find(Member.class, 100L);

            // 조회 쿼리가 날라가지 않음. 1차 캐시에서 가져옴
            Member findMember2 = em.find(Member.class, 100L);

            // 같은 id 엔티티는 동일성(identity) 보장
            // Repeatable read 보장
            System.out.println("result = " + (findMember1 == findMember2));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("트랜젝션을 지원하는 쓰기 지연")
    void write_behind() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            // 1차 캐시에만 저장
            em.persist(member1);
            em.persist(member2);
            System.out.println("===========================");

            // 실제 DB에 insert 쿼리(flush -> commit)
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("변경 감지")
    void dirty_checking() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 영속 엔티티 찾음 - 스냅샷
            Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");

            //em.update(member); << 이런건 필요없음

            System.out.println("==============");

            // 스냅샷 - 현재 앤티티 비교하고 update 쿼리 만듦 -> flush
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("강제 플러시")
    void flush() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member(200L, "member200");

            em.persist(member);

            // tx.commit 때 자동 플러시 되는걸 미리 요청
            // 변경 내역을 DB에 동기화 - 쓰기 지연 SQL 저장소의 모든 내용을 퀴리 실행 & 비우기
            // 영속성 컨텍스트는 비우지 않음.
            em.flush();

            // insert 문이 먼저 나가고 출력된다.
            System.out.println("===========================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("준영속 상태 (detach)")
    void detach() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 영속 엔티티 찾음
            Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");

            // 영속 엔티티중에 1개를 찾아 비움
            em.detach(member);

            System.out.println("===========================");
            // 더티채킹할 내역이 없어서 update 를 하지 않음
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("준영속 상태 (clear)")
    void clear() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 영속 엔티티 찾음
            Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");

            // 영속성 컨텍스트를 비움
            em.clear();
            
            // 영속성 컨텍스트가 비워진 상태라 DB 에서 직접 가져옴
            member = em.find(Member.class, 150L);

            System.out.println("===========================");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
