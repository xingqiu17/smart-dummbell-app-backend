package com.quark.app.service;

import com.quark.app.entity.User;
import com.quark.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /** 注册新用户 */
    public User register(User u) {
        if (repo.existsByAccount(u.getAccount())) {
            throw new IllegalArgumentException("账号已存在");
        }
        return repo.save(u);
    }

    /** 登录：按账号 + 密码查找 */
    public Optional<User> login(String account, String rawPwd) {
        return repo.findByAccount(account)
                   .filter(u -> u.getPassword().equals(rawPwd));
    }

    /** 根据主键查询详情 */
    public User detail(Integer id) {
        return repo.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    /** 保存（用于二次更新昵称等） */
    public User save(User u) {
        return repo.save(u);
    }

}
