package com.quark.app.controller;

import com.quark.app.entity.LogWork;
import com.quark.app.service.LogWorkService;
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
     * @return 该组下的所有动作得分（轻量 DTO，避免懒加载代理序列化问题）
     */
    @GetMapping("/item/{groupId}")
    public ResponseEntity<List<WorkResp>> getWorksByGroup(@PathVariable Integer groupId) {
        List<LogWork> works = workService.getWorksByGroupId(groupId);
        if (works.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // 映射为轻量 DTO，仅返回需要的字段，避免序列化 Hibernate 懒加载代理
        List<WorkResp> resp = works.stream()
                .map(w -> new WorkResp(
                        w.getActionId(),
                        (w.getGroup() != null ? w.getGroup().getGroupId() : groupId),
                        w.getTAcOrder(),
                        w.getScore()
                ))
                .toList();
        return ResponseEntity.ok(resp);
    }

    /** 轻量响应体，字段与 App 端 LogWorkDto 对齐 */
    public static record WorkResp(
            Integer actionId,
            Integer groupId,
            Integer acOrder,
            Integer score
    ) {}
}
