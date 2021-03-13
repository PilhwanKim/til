package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.cascade.Child;
import dev.leonkim.hellojpa.domain.cascade.Parent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

class CascadeTest {
    /**
     * 영속성 전이: CASCADE - 주의
     * - 영속성 전이는 연관관계를 매핑하는 것과 관려이 없음
     * - 엔티티를 영속화할 떼 연관된 엔티티도 함께 영속화하는 편리함을 제공하는 것일 뿐!
     * - ALL, PERSIST 2개만 기억하면 된다.
     * - 1개의 부모 엔티티만 관리할 때만 쓴다(소유자가 유일할 때)
     * - 부모와 자식의 라이프사이클이 동일할 때
     *
     * 영속성 전이 + 고아 객체, 생명 주기
     * - 부모 엔티티를 통해 자식의 생명 주기를 관리할 수 있음.
     * - 도메인 주도 설계(DDD)의 Aggregate Root 개념을 구현할 때 유용
     */

    @Test
    @DisplayName("cascade 옵션 - 영속화를 연계해서 함")
     void cascadeTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Parent parent = new Parent();

            Child child1 = new Child();
            Child child2 = new Child();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("orphanRemoval 옵션 - 고아 객체")
    void orphanRemoval1Test() {
        /**
         * 고아 객체 - 주의
         * - 참조하는 곳이 하나일 때 사용해야 함
         * - 특정 엔티티가 개인 소유할 때 사용
         * - 부모를 제거하면 -> 자식은 고아가 됨 -> 개념적으로 CascadeType.REMOVE 처럼 동작한다.
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Parent parent = new Parent();

            Child child1 = new Child();
            Child child2 = new Child();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            // 고아 객체 발생. 실제로 DB 에서도 삭제
            findParent.getChildList().remove(0);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("orphanRemoval 옵션 - 고아 객체")
    void orphanRemoval2Test() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Parent parent = new Parent();

            Child child1 = new Child();
            Child child2 = new Child();

            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            // 고아 객체 발생. 실제로 DB 에서도 삭제
            em.remove(findParent);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
