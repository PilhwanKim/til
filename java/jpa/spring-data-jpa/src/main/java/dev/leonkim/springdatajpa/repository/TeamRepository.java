package dev.leonkim.springdatajpa.repository;

import dev.leonkim.springdatajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
