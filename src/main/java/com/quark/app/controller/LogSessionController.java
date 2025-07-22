package com.quark.app.controller;

import com.quark.app.entity.LogItem;
import com.quark.app.entity.LogSession;
import com.quark.app.service.LogSessionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 训练记录（头 + 明细）接口
 */
@RestController
@RequestMapping("/api/log/session")
public class LogSessionController {

    private final LogSessionService sessionService;

    public LogSessionController(LogSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // ========== 创建新的训练记录 ==========
    @PostMapping
    public ResponseEntity<LogDayResp> createDayRecord(@RequestBody @Valid LogDayCreateReq req) {
        // 1) 将请求中的明细转换为实体
        List<LogItem> items = req.items().stream()
                .map(i -> {
                    LogItem item = new LogItem();
                    item.setType(i.type());
                    item.setTOrder(i.tOrder());
                    item.setTWeight(i.tWeight());
                    item.setNumber(i.num());
                    item.setTAvgScore(i.avgScore());
                    return item;
                })
                .toList();

        // 2) 调用 Service 保存单条训练记录
        LogSession saved = sessionService.createDayRecord(req.userId(), req.date(), items);

        // 3) 返回包含明细的响应
        var dtoList = sessionService.getDayRecords(req.userId(), req.date());
        // 找到刚创建的那条
        var dto = dtoList.stream()
                .filter(d -> d.session().getRecordId().equals(saved.getRecordId()))
                .findFirst()
                .orElseThrow();
        return ResponseEntity.ok(LogDayResp.from(dto));
    }

    // ========== 查询某用户某日的所有训练记录 ==========
    @GetMapping("/day")
    public ResponseEntity<List<LogDayResp>> getDayRecords(
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        var dtos = sessionService.getDayRecords(userId, date);
        if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        var resp = dtos.stream().map(LogDayResp::from).toList();
        return ResponseEntity.ok(resp);
    }

    /* ======== 请求 / 响应 DTO ======== */

    /** 创建训练记录的请求体 */
    public record LogDayCreateReq(
            Integer userId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            List<LogItemReq> items
    ) { }

    /** 单个运动动作请求体 */
    public record LogItemReq(
            Integer type,
            Integer tOrder,
            Integer tWeight,
            Integer num,
            Integer avgScore
    ) { }

    /** 返回给前端的完整训练记录 */
    public record LogDayResp(
            LogSession session,
            List<LogItem> items
    ) {
        public static LogDayResp from(com.quark.app.service.LogSessionService.LogDayDto dto) {
            return new LogDayResp(dto.session(), dto.items());
        }
    }
}
