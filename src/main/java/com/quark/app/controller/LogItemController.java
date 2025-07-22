package com.quark.app.controller;

import com.quark.app.entity.LogItem;
import com.quark.app.service.LogItemService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 训练记录动作组（LogItem）接口
 */
@RestController
@RequestMapping("/api/log/item")
public class LogItemController {

    private final LogItemService itemService;

    public LogItemController(LogItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 查询某次训练记录下的所有运动组（按 t_order 升序）
     *
     * GET /api/log/item/session/{recordId}
     *
     * @param recordId 训练记录 ID
     * @return 该记录下的所有 LogItem 列表
     */
    @GetMapping("/session/{recordId}")
    public ResponseEntity<List<LogItem>> getItemsBySession(
            @PathVariable @Positive Integer recordId
    ) {
        List<LogItem> items = itemService.getItemsBySessionId(recordId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }
}
