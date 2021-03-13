package dev.leonkim.jpashop.domain;

import dev.leonkim.hellojpa.domain.base.BaseEntity;
import dev.leonkim.hellojpa.domain.value.Address;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

}
