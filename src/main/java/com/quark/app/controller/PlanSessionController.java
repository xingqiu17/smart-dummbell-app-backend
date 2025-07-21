package com.quark.app.controller;

import com.quark.app.entity.PlanItem;
import com.quark.app.entity.PlanSession;
import com.quark.app.service.PlanSessionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 训练计划（头 + 明细）接口
 */
@RestController
@RequestMapping("/api/plan/session")
public class PlanSessionController {

    private final PlanSessionService sessionService;

    public PlanSessionController(PlanSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // ========== 创建某一天的完整计划 ==========
    @PostMapping
    public ResponseEntity<PlanDayResp> createDayPlan(@RequestBody @Valid PlanDayCreateReq req) {
        // 1) 将请求中的明细转换为实体
        List<PlanItem> items = req.items().stream()
                .map(i -> {
                    PlanItem item = new PlanItem();
                    item.setType(i.type());
                    item.setNumber(i.number());
                    item.setTOrder(i.tOrder());
                    item.setTWeight(i.tWeight());
                    item.setComplete(false);
                    return item;
                })
                .toList();

        // 2) 调用 Service 保存
        PlanSession saved = sessionService.createDayPlan(req.userId(), req.date(), items);

        // 3) 返回包含明细的响应
        var dto = sessionService.getDayPlan(req.userId(), req.date()).orElseThrow();
        return ResponseEntity.ok(PlanDayResp.from(dto));
    }

    // ========== 查询某用户某日计划 ==========
    @GetMapping("/day")
    public ResponseEntity<PlanDayResp> getDayPlan(
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return sessionService.getDayPlan(userId, date)
                .map(PlanDayResp::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* ======== 请求 / 响应 DTO ======== */

    /** 创建计划的请求体 */
    public record PlanDayCreateReq(
            Integer userId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            List<PlanItemReq> items
    ) { }

    /** 单个动作请求体 */
    public record PlanItemReq(
            Integer type,
            Integer number,
            Integer tOrder,
            Integer tWeight
    ) { }

    /** 返回给前端的完整计划 */
    public record PlanDayResp(
            PlanSession session,
            List<PlanItem> items
    ) {
        public static PlanDayResp from(PlanSessionService.PlanDayDto dto) {
            return new PlanDayResp(dto.session(), dto.items());
        }
    }
}

