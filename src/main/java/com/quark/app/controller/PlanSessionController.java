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
import java.util.stream.Collectors;

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

        // 2) 调用 Service 保存（此时不再覆盖旧记录）
        PlanSession saved = sessionService.createDayPlan(req.userId(), req.date(), items);

        // 3) 组装并返回新创建的那一条
        //    也可以返回所有当日计划，但这里保留原来只返回单条的逻辑
        var dto = new com.quark.app.service.PlanSessionService.PlanDayDto(
            saved,
            items
        );
        return ResponseEntity.ok(PlanDayResp.from(dto));
    }

    // ========== 查询某用户某日所有计划 ==========
    @GetMapping("/day")
    public ResponseEntity<List<PlanDayResp>> getDayPlans(
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<PlanDayResp> respList = sessionService
            .getDayPlans(userId, date)                                // 新方法，返回 List<PlanDayDto>
            .stream()
            .map(PlanDayResp::from)                                   // 转成前端 DTO
            .collect(Collectors.toList());

        return ResponseEntity.ok(respList);
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
        public static PlanDayResp from(com.quark.app.service.PlanSessionService.PlanDayDto dto) {
            return new PlanDayResp(dto.session(), dto.items());
        }
    }
}
