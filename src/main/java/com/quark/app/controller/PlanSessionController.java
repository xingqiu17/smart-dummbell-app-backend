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

    // ========== 创建某一天的完整计划（只返回新建的那条） ==========
    @PostMapping
    public ResponseEntity<PlanDayResp> createDayPlan(@RequestBody @Valid PlanDayCreateReq req) {
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

        PlanSession saved = sessionService.createDayPlan(req.userId(), req.date(), items);
        var dto = new com.quark.app.service.PlanSessionService.PlanDayDto(saved, items);
        return ResponseEntity.ok(PlanDayResp.from(dto));
    }

    // ========== 查询某用户某日所有计划（返回数组） ==========
    @GetMapping("/day")
    public ResponseEntity<List<PlanDayResp>> getDayPlans(
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<PlanDayResp> respList = sessionService
            .getDayPlans(userId, date)                       // 新方法，返回 List<PlanDayDto>
            .stream()
            .map(PlanDayResp::from)
            .collect(Collectors.toList());

        // 你可以在这里加个日志，确认走到这：
        System.out.println(">> getDayPlans: found " + respList.size() + " sessions for " + date);

        return ResponseEntity.ok(respList);
    }

    // ======== DTO 定义 ========

    public record PlanDayCreateReq(
        Integer userId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        List<PlanItemReq> items
    ) {}

    public record PlanItemReq(
        Integer type,
        Integer number,
        Integer tOrder,
        Float tWeight
    ) {}

    public record PlanDayResp(
        PlanSession session,
        List<PlanItem> items
    ) {
        public static PlanDayResp from(com.quark.app.service.PlanSessionService.PlanDayDto dto) {
            return new PlanDayResp(dto.session(), dto.items());
        }
    }
}
