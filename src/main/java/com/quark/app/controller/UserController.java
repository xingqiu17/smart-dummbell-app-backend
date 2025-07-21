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
    /* ---------- 注册：仅账号 + 密码，其余写默认值 ---------- */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterReq req) {
        User u = new User();
        u.setAccount(req.account());
        u.setHwId(0);
        u.setPassword(req.password());           // ← 记得 Service 里做 MD5+盐
        u.setGender(0);
        u.setBirthday(LocalDate.of(2000, 1, 1));
        u.setHeight(0f);
        u.setWeight(0f);
        u.setAim(0);
        u.setHwWeight(0f);

        u.setName("用户");  // ← 先给一个非空的临时名字

        // ① 先保存，拿到自增 ID
        u = userService.register(u);

        // ② 再用带 ID 的名字更新一次
        u.setName("用户" + u.getUserId());
        u = userService.save(u);

        return ResponseEntity.ok(u);
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


    // UserController.java 内部新增
    @PostMapping("/{id}/trainData")
    public ResponseEntity<User> updateTrainData(
            @PathVariable Integer id,
            @RequestBody @Valid TrainDataReq req) {

        User updated = userService.updateTrainData(id, req.aim(), req.hwWeight());
        return ResponseEntity.ok(updated);
    }

    public record TrainDataReq(
            @NotNull Integer aim,      // 0=无目标 1=手臂 ……
            @NotNull @Positive Float hwWeight
    ) {}

    @PostMapping("/{id}/name")
    public ResponseEntity<User> updateName(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateNameReq req) {
        User updated = userService.updateName(id, req.name());
        return ResponseEntity.ok(updated);
    }

    public record UpdateNameReq(
            @NotBlank @Size(max = 16) String name
    ) {}

    /**
     * 修改身体数据：出生日期、身高、体重、性别
     */
    @PostMapping("/{id}/body")
    public ResponseEntity<User> updateBodyData(
            @PathVariable Integer id,
            @RequestBody @Valid BodyDataReq req) {
        User updated = userService.updateBodyData(
                id,
                req.birthday(),
                req.height(),
                req.weight(),
                req.gender()
        );
        return ResponseEntity.ok(updated);
    }

    public record BodyDataReq(
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate birthday,

            @NotNull @Positive Float height,
            @NotNull @Positive Float weight,

            @NotNull @Min(0) @Max(2) Integer gender
    ) {}


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
            @NotNull @Min(0) @Max(2) Integer gender,
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
