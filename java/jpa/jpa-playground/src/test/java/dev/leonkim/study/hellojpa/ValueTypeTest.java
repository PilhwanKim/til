package dev.leonkim.study.hellojpa;

import dev.leonkim.hellojpa.domain.Member;
import dev.leonkim.hellojpa.domain.value.Address;
import dev.leonkim.hellojpa.domain.value.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

class ValueTypeTest {
    /**
     * 엔티티를 좀더 객체지향적으로 만드는 value type
     * - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
     * - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능하다.
     * - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음
     * - 임베디드 타입의 값이  null 이면 매핑한 칼럼 값 모두 null
     * - 객체의 공유 참조는 피할 수 없다. (조심해서 사용해야 한다.)
     * - 값 타입은 무조건 불변 객체로 설계해야 한다.(side-effect 원천 차단)
     * - setter 없앰, 생성자만 값을 설정
     * - 값 타입 컬랙션은 CASCADE.ALL + 고아객체 제거 기능을 필수로 가진거나 같다.
     *
     *  엔티티 타입의 특징
     *  - 식별자 O
     *  - 생명 주기 관리
     *  - 공유 가능
     *  값 타입의 특징
     *  - 식별자 X
     *  - 생명 주기를 엔티티에 의존
     *  - 공유하지 않는것이 안전(복사해서 사용)
     *  - 불변 객체로 만드는 것이 안전
     */

    @Test
    @DisplayName("임베디드 타입")
     void embeddedTypeTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setName("hello");
            member.setHomeAddress(new Address("city", "street", "1000"));
            member.setPeriod(new Period());

            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("값 타입 공유 참조 문제")
    void valueTypeSharedReferenceIssueTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setName("member1");
            Address address = new Address("city", "street", "1000");
            member1.setHomeAddress(address);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
            member2.setHomeAddress(address);
            em.persist(member2);

            // 한참 지난 후

            /**
             * 두 맴버가 다 newCity 로 update 된다.
             * - 값 타입을 여러 엔티티에서 공유하면 위험하다.
             * - 부작용(side effect) 발생
             * - 대신 값(인스턴스)를 복사해서 사용
             */
            member1.getHomeAddress().setCity("newCity");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("값 타입 공유 참조 문제 - 해결책은 객체 복사를 한다.")
    void valueTypeSharedReferenceSolvedTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setName("member1");
            Address address = new Address("city", "street", "1000");
            member1.setHomeAddress(address);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
            Address address2 = new Address(address.getCity(), address.getStreet(), address.getStreet());
            member2.setHomeAddress(address2);
            em.persist(member2);

            // 한참 지난 후

            member1.getHomeAddress().setCity("newCity");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("값 타입 컬랙션 - 생성, 조회")
    void valueTypeCollectionReadTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setName("member1");
            member.setHomeAddress(new Address("city1", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("=========== START =============");
            Member findMember = em.find(Member.class, member.getId());

            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = " + address.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
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
    @DisplayName("값 타입 컬랙션 - 수정")
    void valueTypeCollectionUpdateTest() {
        /**
         * 값 타입은 엔티티와 다르게 식별자 개념이 없다
         * ->
         * 값 타입 컬렉션에 변경 사항이 발생하면,
         * 주인 엔티티와 연관된 모든 데이터를 삭제하고,
         * 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
         * -> null 입력 X, 중복 저장 X
         * -> 실무에서는 값 타입 컬랙션 대신 일대다 관계(엔티티화) 고려
         * -> 영속성 전이(Cascade) + 고아 객체 제거 사용
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setName("member1");
            member.setHomeAddress(new Address("city1", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("=========== START =============");
            Member findMember = em.find(Member.class, member.getId());

            // homeCity -> newCity
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));

            // 치킨 -> 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // old1 -> newCity1
            findMember.getAddressHistory().remove(new Address("old1", "street", "10000"));
            findMember.getAddressHistory().add(new Address("newCity1", "street", "10000"));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
