package com.hany.stock.repository;

import com.hany.stock.domain.entity.User;
import com.hany.stock.enum_class.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<User> findByLoginId(String loginId);
    Long countAllByUserRole(UserRole userRole);
    Page<User> findAllByNicknameContains(String nickname, PageRequest pageRequest);

}
