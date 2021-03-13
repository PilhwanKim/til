package dev.leonkim.hellojpa.domain;

import dev.leonkim.hellojpa.domain.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProduct extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int count;

    private int price;

    private LocalDateTime orderDateTime;

}
