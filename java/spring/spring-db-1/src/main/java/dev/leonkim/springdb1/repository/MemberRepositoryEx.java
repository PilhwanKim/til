package dev.leonkim.springdb1.repository;

import dev.leonkim.springdb1.domain.Member;

import java.sql.SQLException;

/**
 * SQLException 이 체크 예외이기 때문에
 * JDBC 기술에 종속적인 인터페이스가 된다.
 */
public interface MemberRepositoryEx {
    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}
