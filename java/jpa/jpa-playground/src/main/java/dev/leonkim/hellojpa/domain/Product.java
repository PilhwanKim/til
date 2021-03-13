package dev.leonkim.hellojpa.domain;

import dev.leonkim.hellojpa.domain.base.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Product extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockAmount;

    // 다대다는 거의 실제로는 사용 X
    // - 연결 테이블이 단순히 연결만 하고 끝나지 않음
    // - 내가 원하는 쿼리 방식대로 작동하지 않음
//    @ManyToMany(mappedBy = "products")
//    private List<Member> members = new ArrayList<>();

    // 중간 Entity 를 만들고 1:n 연관관계로 분해한다.
    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();

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

    public int getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }
}
