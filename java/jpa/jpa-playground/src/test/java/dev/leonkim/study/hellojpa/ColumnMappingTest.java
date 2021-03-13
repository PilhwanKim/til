package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.Member;
import dev.leonkim.hellojpa.domain.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

class ColumnMappingTest {
    /**
     * 주의
     * - Enum Type 은 ORDINAL로 쓰지 말자 무조건 STRING 으로 한다.
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
            member.setName("leonkim");
            member.setRoleType(RoleType.USER);

            // IDENTITY 와 SEQUENCE 전략이 내부 동작이 다름
            System.out.println("=============");
            em.persist(member);
            System.out.println("member.id = " + member.getId());
            System.out.println("=============");
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
