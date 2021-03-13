package dev.leonkim.springdatajpa.repository;

import dev.leonkim.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
