package com.quark.app.controller;

import com.quark.app.entity.PlanItem;
import com.quark.app.entity.PlanSession;
import com.quark.app.service.PlanItemService;
import com.quark.app.service.PlanSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
    private final PlanItemService    itemService;


    public PlanSessionController(PlanSessionService sessionService, PlanItemService itemService) {
        this.sessionService = sessionService;
        this.itemService = itemService;
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
                item.setRest(i.rest());
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


     // ===== ① 完整更新计划头 & 动作明细 =====
    @PutMapping("/{sessionId}")
    public ResponseEntity<Void> updateSessionAndItems(
            @PathVariable Integer sessionId,
            @RequestBody @Valid PlanSessionUpdateReq req
    ) {
        // 1) 更新头表（userId, date）
        sessionService.updateSession(sessionId, req.userId(), req.date());

        // 2) 删除旧明细、批量保存新明细
        itemService.deleteBySessionId(sessionId);
        List<PlanItem> newItems = req.items().stream().map(i -> {
            PlanItem pi = new PlanItem();
            pi.setSession(sessionService.findById(sessionId));
            pi.setType(i.type());
            pi.setNumber(i.number());
            pi.setTOrder(i.tOrder());
            pi.setTWeight(i.tWeight());
            pi.setRest(i.rest());

            return pi;
        }).collect(Collectors.toList());
        itemService.saveAll(newItems);

        return ResponseEntity.noContent().build();
    }

    public static record PlanSessionUpdateReq(
            @NotNull Integer userId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate date,
            @NotNull List<PlanItemUpdateReq> items
    ) {}
    public static record PlanItemUpdateReq(
            @NotNull Integer type,
            @NotNull Integer number,
            @NotNull Integer tOrder,
            @NotNull Float   tWeight,
            @NotNull Integer rest
    ) {}


    // ===== ② 仅更新 complete 标志 =====
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<Void> updateComplete(
            @PathVariable Integer sessionId,
            @RequestBody @Valid CompleteReq req
    ) {
        sessionService.updateCompleteFlag(sessionId, req.complete());
        return ResponseEntity.noContent().build();
    }

    public static record CompleteReq(
            @NotNull Boolean complete
    ) {}


    // ===== ③ 删除整个计划（头 + 明细） =====
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Integer sessionId
    ) {
        // 删除明细
        itemService.deleteBySessionId(sessionId);
        // 删除头
        sessionService.deleteById(sessionId);
        return ResponseEntity.noContent().build();
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
        Float tWeight,
        Integer rest
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
