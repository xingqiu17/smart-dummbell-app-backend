package com.quark.app.controller;

import com.quark.app.entity.User;
import com.quark.app.service.UserService;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * 用户接口  /api/v1/users
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // 构造注入（Spring Boot 3 推荐）
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* ------------------------------------------------------------------ */
    /* 注册                                                                */
    /* ------------------------------------------------------------------ */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterReq req) {
        User u = new User();
        u.setHwId(req.hwId());
        u.setAccount(req.account());
        u.setPassword(req.password());
        u.setName(req.name());
        u.setGender(req.gender());
        u.setHeight(req.height());
        u.setWeight(req.weight());
        u.setBirthday(req.birthday());
        u.setAim(req.aim());
        User saved = userService.register(u);          // 由 Service 做账号重复校验
        return ResponseEntity.ok(saved);
    }

    /* ------------------------------------------------------------------ */
    /* 登录：成功返回用户信息，失败返回 401                                 */
    /* ------------------------------------------------------------------ */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginReq req) {
        return userService.login(req.account(), req.password())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401)
                        .body(Map.of("error", "账号或密码错误")));
    }

    /* ------------------------------------------------------------------ */
    /* 查询详情                                                            */
    /* ------------------------------------------------------------------ */
    @GetMapping("/{id}")
    public ResponseEntity<User> detail(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.detail(id));
    }

    /* ================================================================== */
    /* ====== 简单 DTO —— 用 Java 22 record 写法 ========================= */
    /* ================================================================== */

    /**
     * 注册请求体
     */
    public record RegisterReq(
            @NotNull           Integer hwId,
            @NotBlank @Size(max = 16) String account,
            @NotBlank @Size(max = 32) String password,
            @NotBlank @Size(max = 16) String name,
            @NotNull @Min(0) @Max(1) Integer gender,
            @NotNull @Positive Float height,
            @NotNull @Positive Float weight,
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthday,
            @NotNull Integer aim
    ) {}

    /**
     * 登录请求体
     */
    public record LoginReq(
            @NotBlank String account,
            @NotBlank String password
    ) {}
}
