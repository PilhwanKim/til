package dev.leonkim.springdatajpa.repository;

import dev.leonkim.springdatajpa.entity.Member;
import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
