package com.quark.app.controller;

import com.quark.app.entity.Plan;
import com.quark.app.service.PlanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 计划接口  /api/v1/plans
 */
@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    /* ------------------------------------------------------------------ */
    /* 新增计划                                                            */
    /* ------------------------------------------------------------------ */
    @PostMapping
    public ResponseEntity<Plan> addPlan(@RequestBody @Valid AddPlanReq req) {
        Plan p = new Plan();
        p.setUserId(req.userId());
        p.setDate(req.date());
        p.setType(req.type());
        p.setNumber(req.number());
        p.setTOrder(req.tOrder());
        p.setTWeight(req.tWeight());
        p.setCompelete(false); // 初始为未完成
        Plan saved = planService.addPlan(p);
        return ResponseEntity.ok(saved);
    }

    /* ------------------------------------------------------------------ */
    /* 查询详情                                                            */
    /* ------------------------------------------------------------------ */
    @GetMapping("/{id}")
    public ResponseEntity<Plan> detail(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.detail(id));
    }

    /* ------------------------------------------------------------------ */
    /* 列表：按用户或按用户+日期                                          */
    /* ------------------------------------------------------------------ */
    @GetMapping
    public ResponseEntity<List<Plan>> list(
            @RequestParam @NotNull Integer userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<Plan> list = (date == null)
                ? planService.listByUser(userId)
                : planService.listByUserAndDate(userId, date);
        return ResponseEntity.ok(list);
    }

    /* ------------------------------------------------------------------ */
    /* 更新计划                                                            */
    /* ------------------------------------------------------------------ */
    @PutMapping("/{id}")
    public ResponseEntity<Plan> update(
            @PathVariable Integer id,
            @RequestBody @Valid UpdatePlanReq req
    ) {
        Plan p = planService.detail(id);
        // 只更新可变字段
        p.setType(req.type());
        p.setNumber(req.number());
        p.setTOrder(req.tOrder());
        p.setTWeight(req.tWeight());
        p.setCompelete(req.compelete());
        Plan updated = planService.updatePlan(p);
        return ResponseEntity.ok(updated);
    }

    /* ------------------------------------------------------------------ */
    /* 删除计划                                                            */
    /* ------------------------------------------------------------------ */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        planService.deletePlan(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    /* ================================================================== */
    /* ======== DTO —— Java 22 record 写法 =============================== */
    /* ================================================================== */

    /** 新增计划请求体 */
    public record AddPlanReq(
            @NotNull Integer userId,
            @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @NotNull @Min(0) Integer type,
            @NotNull @Min(1) Integer number,
            @NotNull @Min(0) Integer tOrder,
            @NotNull @Min(0) Integer tWeight
    ) {}

    /** 更新计划请求体 */
    public record UpdatePlanReq(
            @NotNull @Min(0) Integer type,
            @NotNull @Min(1) Integer number,
            @NotNull @Min(0) Integer tOrder,
            @NotNull @Min(0) Integer tWeight,
            @NotNull Boolean compelete
    ) {}
}
