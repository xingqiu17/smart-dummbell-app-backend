package com.quark.app.repository;

import com.quark.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    /** 用账号查找用户 */
    Optional<User> findByAccount(String account);

    /** 判断账号是否已存在 */
    boolean existsByAccount(String account);
}
