package com.example.dracoworld.repository;

import com.example.dracoworld.domain.Member;
import java.util.List;
import javax.management.relation.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByEmailAndStatusIsTrue(String username);

	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	List<Member> findByRoleNot(Role role);
}
