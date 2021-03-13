package dev.leonkim.hellojpa.domain;

import dev.leonkim.hellojpa.domain.base.BaseEntity;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
//    @BatchSize(size = 100)
    private List<Member> members = new ArrayList<>();

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

    public List<Member> getMembers() {
        return members;
    }

//    연관관계 편의 메서드는 주로 한쪽에만 넣기 - 무한 루프 문제가 생길 수 있음
//    public void addMember(Member member) {
//        member.setTeam(this);
//        members.add(member);
//    }

}
