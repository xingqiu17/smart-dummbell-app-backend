package com.quark.app.controller;

import com.quark.app.entity.LogWork;
import com.quark.app.service.LogWorkService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 训练记录动作明细（LogWork）接口
 */
@RestController
@RequestMapping("/api/log/work")
public class LogWorkController {

    private final LogWorkService workService;

    public LogWorkController(LogWorkService workService) {
        this.workService = workService;
    }

    /**
     * 查询某个运动组下的所有动作得分（按 ac_order 升序）
     *
     * GET /api/log/work/item/{groupId}
     *
     * @param groupId 运动组 ID
     * @return 该组下的所有 LogWork 列表
     */
    @GetMapping("/item/{groupId}")
    public ResponseEntity<List<LogWork>> getWorksByGroup(
            @PathVariable @Positive Integer groupId
    ) {
        List<LogWork> works = workService.getWorksByGroupId(groupId);
        if (works.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(works);
    }
}
