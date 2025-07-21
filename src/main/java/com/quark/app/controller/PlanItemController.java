package com.quark.app.controller;

import com.quark.app.entity.PlanItem;
import com.quark.app.service.PlanItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 训练计划 – 动作明细接口
 */
@RestController
@RequestMapping("/api/plan/item")
public class PlanItemController {

    private final PlanItemService itemService;

    public PlanItemController(PlanItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 根据 sessionId 查询该训练计划下的全部动作
     * 例：GET /plan/item/list?sessionId=123
     */
    @GetMapping("/list")
    public List<PlanItem> listBySession(@RequestParam Integer sessionId) {
        return itemService.listItemsBySession(sessionId);
    }
}
