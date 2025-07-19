package com.quark.app.controller;

import com.quark.app.entity.LogWork;
import com.quark.app.service.LogWorkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 训练动作评分明细接口  /api/v1/logworks
 */
@RestController
@RequestMapping("/api/v1/logworks")
public class LogWorkController {

    private final LogWorkService service;

    public LogWorkController(LogWorkService service) {
        this.service = service;
    }

    /* ------------------------------------------------------------------ */
    /* 新增动作评分记录                                                    */
    /* ------------------------------------------------------------------ */
    @PostMapping
    public ResponseEntity<LogWork> add(
            @RequestBody @Valid AddLogWorkReq req
    ) {
        LogWork lw = new LogWork();
        lw.setGroupId(req.groupId());
        lw.setScore(req.score());
        LogWork saved = service.addLogWork(lw);
        return ResponseEntity.ok(saved);
    }

    /* ------------------------------------------------------------------ */
    /* 查询单条评分记录                                                    */
    /* ------------------------------------------------------------------ */
    @GetMapping("/{id}")
    public ResponseEntity<LogWork> detail(@PathVariable Integer id) {
        return ResponseEntity.ok(service.detail(id));
    }

    /* ------------------------------------------------------------------ */
    /* 列表：按训练组查询                                                  */
    /* ------------------------------------------------------------------ */
    @GetMapping
    public ResponseEntity<List<LogWork>> listByGroup(
            @RequestParam @NotNull Integer groupId
    ) {
        return ResponseEntity.ok(service.listByGroup(groupId));
    }

    /* ------------------------------------------------------------------ */
    /* 更新评分记录                                                        */
    /* ------------------------------------------------------------------ */
    @PutMapping("/{id}")
    public ResponseEntity<LogWork> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateLogWorkReq req
    ) {
        LogWork lw = service.detail(id);
        lw.setGroupId(req.groupId());
        lw.setScore(req.score());
        LogWork updated = service.updateLogWork(lw);
        return ResponseEntity.ok(updated);
    }

    /* ------------------------------------------------------------------ */
    /* 删除评分记录                                                        */
    /* ------------------------------------------------------------------ */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.deleteLogWork(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    /* ================================================================== */
    /* ======== DTO —— Java 22 record 写法 =============================== */
    /* ================================================================== */

    public record AddLogWorkReq(
            @NotNull Integer groupId,
            @NotNull @Min(0) Integer score
    ) {}

    public record UpdateLogWorkReq(
            @NotNull Integer groupId,
            @NotNull @Min(0) Integer score
    ) {}
}
