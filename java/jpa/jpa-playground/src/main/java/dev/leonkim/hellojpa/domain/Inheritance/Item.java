package dev.leonkim.hellojpa.domain.Inheritance;


import dev.leonkim.hellojpa.domain.base.BaseEntity;

import javax.persistence.*;

@Entity(name = "bItem")
@Table(name = "bItem")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Item extends BaseEntity {
    /**
     * 관계형 데이터베이스는 상속관계 없음
     * 모델링 기법 - 슈퍼타입-서브타입 관계
     * 상속 전략
     * - JOINED: 조인 전략(default 전략)
     *      - 각자 타입별로 테이블 존재.
     *      - 가장 정규화된 형태 (중복내용 X)
     *      - 저장공간 효율화
     *      - 조회시 조인 복잡해짐. insert늘 2번 한다.
     *      - @DiscriminatorColumn, @DiscriminatorValue
     * - SINGLE_TABLE: 단일 테이블 전략(성능 고려시)
     *      - 한 테이블에 각 프로퍼티를 모두 넣는다
     *      - null 허용이 많아짐
     *      - 오히려 테이블이 커져서 조회 성능이 느려질 수 있음
     *      - 성능상 이점(1개 insert, 1개 select)
     * - TABLE_PER_CLASS: 구현 클래스마다 테이블 전략 (사용 X)
     *      - 상위 테이블 없음
     *      - 상위 타입으로 전체 조회할때 union 으로 조회하므로 비효율적임
     *      - 사실상 사용 X. 위의 2개 전략에 비해 장점이 없음
     */

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
