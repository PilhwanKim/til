package dev.leonkim.hellojpa.domain;

import dev.leonkim.hellojpa.domain.base.BaseEntity;
import dev.leonkim.hellojpa.domain.value.Address;
import dev.leonkim.hellojpa.domain.value.Period;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import static javax.persistence.CascadeType.ALL;

@Entity(name = "bMember")
@Table(name = "bMember")
@NamedQuery(
        name = "bMember.findByUsername",
        query = "select m from bMember m where m.username = :username"
)
public class Member extends BaseEntity {
    /**
     * 직접 할당 - @Id만 사용
     * 자동 생성 - @GeneratedValue 까지 붙임
     *        - AUTO - DB 방언에 맞춰서 자동으로 만듦
     *        - IDENTITY - 기본키 생성을 DB에 위임. 이 전략만 id를 DB가 최초에 부여하기 때문에,
     *                      persist() 호출 시점에 insert 날림
     *        - SEQUENCE - SEQUENCE 오브젝트에서 키값을 가져옴
     *        - TABLE - 키를 생성 전용 Table 을 마련. 성능 이슈 있음
     * 기본 키 권장: Long type (10억개 이상) + 대체키(자연키는 변경 가능성 늘 존재) + 키 생성전략(Auto Increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String username;

    private Integer age;

// ProxyAndLodingTest.lazyLoading 에서 실행해서 차이점을 확인해보자
//    @ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    // 다대다는 거의 실제로는 사용 X
    // - 연결 테이블이 단순히 연결만 하고 끝나지 않음
    // - 내가 원하는 쿼리 방식대로 작동하지 않음
//    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT")
//    private List<Product> products = new ArrayList<>();

    // 중간 Entity 를 만들고 1:n 연관관계로 분해한다.
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT")
    private List<Product> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Lob
    private String description;

    @Transient
    private int temp;

    @Embedded
    private Period period;

    @Embedded
    private Address homeAddress;

    /**
     * 한 엔티티에 같은 값 타입을 사용하면?
     * - 칼럼 명이 중복됨
     * - @AttributeOverrides(임베디드 타입), @AttributeOverride(임베디드 타입 각 프로퍼티별) 사용하여 칼럼명 재정의
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="city",
                    column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name="street",
                    column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name="zipcode",
                    column = @Column(name = "WORK_ZIPCODE")),
    })
    private Address workAddress;

    /**
     * 값 타입 컬렉션
     * - 값 타입을 하나 이상 저장할 때 사용
     * - 데이터 베이스 클렉션을 같은 테이블에 저장할 수 없다.
     * - 컬렉션을 저장하기 위한 별도의 테이블이 필요함.
     */
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
        @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns =
        @JoinColumn(name = "MEMBER_ID")
    )
    private List<Address> addressHistory = new ArrayList<>();

    // 값 타입 컬렉션 대안
//    @OneToMany(cascade = ALL, orphanRemoval = true)
//    @JoinColumn(name = "MEMBER_ID")
//    private List<AddressEntity> addressHistory = new ArrayList<>();

    public Member() {
    }

    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public List<Address> getAddressHistory() {
        return addressHistory;
    }

    // 연관관계 편의 메서드 - 순수 객체 상태 맞추기
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
