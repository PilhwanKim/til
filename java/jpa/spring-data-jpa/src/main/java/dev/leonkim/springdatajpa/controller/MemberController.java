package dev.leonkim.springdatajpa.controller;

import dev.leonkim.springdatajpa.dto.MemberDto;
import dev.leonkim.springdatajpa.entity.Member;
import dev.leonkim.springdatajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터 (JPA 에서 MVC 기능 확장됨. 권장하진 않음) - API 에 엔티티 구조가 공개됨. 단순 조회용으로만 사용
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // 웹에서도 페이징과 정렬 객체 이용 가능.
    @GetMapping("/members")
    public Page<MemberDto> findMembers(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        // 엔티티를 API 에 노출, 의존 하지 않기 위해 DTO로 반환하자.
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    // 패이징 결과를 보기 위해 member 엔티티를 미리 넣음
    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
