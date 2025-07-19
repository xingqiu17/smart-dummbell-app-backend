package com.quark.app.controller;

import com.quark.app.entity.Log;
import com.quark.app.service.LogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 训练记录接口  /api/v1/logs
 */
@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /* ------------------------------------------------------------------ */
    /* 新增训练记录                                                        */
    /* ------------------------------------------------------------------ */
    @PostMapping
    public ResponseEntity<Log> addLog(@RequestBody @Valid AddLogReq req) {
        Log log = new Log();
        log.setUserId(req.userId());
        log.setType(req.type());
        log.setTOrder(req.tOrder());
        log.setTWeight(req.tWeight());
        log.setTTime(req.tTime());
        log.setNum(req.num());
        log.setAvgScore(req.avgScore());
        Log saved = logService.addLog(log);
        return ResponseEntity.ok(saved);
    }

    /* ------------------------------------------------------------------ */
    /* 查询单条训练记录                                                    */
    /* ------------------------------------------------------------------ */
    @GetMapping("/{id}")
    public ResponseEntity<Log> detail(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(logService.detail(id));
    }

    /* ------------------------------------------------------------------ */
    /* 列表查询：支持按用户、按动作类型、按时间区间                          */
    /* ------------------------------------------------------------------ */
    @GetMapping
    public ResponseEntity<List<Log>> list(
            @RequestParam @NotNull Integer userId,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<Log> result;
        if (type != null && start == null && end == null) {
            // 按用户+动作类型
            result = logService.listByUserAndType(userId, type);
        } else if (start != null && end != null) {
            // 按用户+时间区间
            result = logService.listByUserAndPeriod(userId, start, end);
        } else {
            // 仅按用户查询
            result = logService.listByUser(userId);
        }
        return ResponseEntity.ok(result);
    }

    /* ------------------------------------------------------------------ */
    /* 更新训练记录                                                        */
    /* ------------------------------------------------------------------ */
    @PutMapping("/{id}")
    public ResponseEntity<Log> update(
            @PathVariable("id") Integer id,
            @RequestBody @Valid UpdateLogReq req
    ) {
        Log log = logService.detail(id);
        log.setType(req.type());
        log.setTOrder(req.tOrder());
        log.setTWeight(req.tWeight());
        log.setTTime(req.tTime());
        log.setNum(req.num());
        log.setAvgScore(req.avgScore());
        Log updated = logService.updateLog(log);
        return ResponseEntity.ok(updated);
    }

    /* ------------------------------------------------------------------ */
    /* 删除训练记录                                                        */
    /* ------------------------------------------------------------------ */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        logService.deleteLog(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    /* ================================================================== */
    /* ======== DTO —— Java 22 record 写法 =============================== */
    /* ================================================================== */

    /** 新增训练记录请求体 */
    public record AddLogReq(
            @NotNull Integer userId,
            @NotNull @Min(0) Integer type,
            @NotNull @Min(0) Integer tOrder,
            @NotNull @Min(0) Integer tWeight,
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tTime,
            @NotNull @Min(1) Integer num,
            @NotNull @Min(0) Integer avgScore
    ) {}

    /** 更新训练记录请求体 */
    public record UpdateLogReq(
            @NotNull @Min(0) Integer type,
            @NotNull @Min(0) Integer tOrder,
            @NotNull @Min(0) Integer tWeight,
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tTime,
            @NotNull @Min(1) Integer num,
            @NotNull @Min(0) Integer avgScore
    ) {}
}
