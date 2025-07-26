package com.quark.app.controller;

import com.quark.app.entity.LogItem;
import com.quark.app.entity.LogSession;
import com.quark.app.service.LogSessionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
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

    // ========== 创建新的训练记录（一次请求写入 session + items + works） ==========
    @PostMapping
    public ResponseEntity<LogDayResp> createDayRecord(@RequestBody @Valid LogDayCreateReq req) {
        // 1) 将请求中的明细（含可选 works）映射为 Service 所需的 DTO
        List<LogSessionService.LogItemCreate> itemsWithWorks =
                (req.items() == null ? Collections.<LogItemReq>emptyList() : req.items())
                        .stream()
                        .map(i -> new LogSessionService.LogItemCreate(
                                i.type(),
                                i.num(),
                                i.tOrder(),
                                i.tWeight(),
                                i.avgScore(),  // 允许为 null，Service 默认置 0
                                (i.works() == null ? Collections.<LogWorkReq>emptyList() : i.works())
                                        .stream()
                                        .map(w -> new LogSessionService.LogWorkCreate(
                                                w.acOrder(),
                                                w.score() // 允许为 null，Service 默认置 0
                                        ))
                                        .toList()
                        ))
                        .toList();

        // 2) 调用 Service：一次性保存 session + items + works（方案 A：显式保存）
        LogSession saved = sessionService.createDayRecordWithWorks(
                req.userId(), req.date(), itemsWithWorks
        );

        // 3) 为了与原有响应结构保持一致（session + items），此处重查当日并挑出刚创建的那条
        var dtoList = sessionService.getDayRecords(req.userId(), req.date());
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

    /** 创建训练记录的请求体（items 下支持可选 works） */
    public record LogDayCreateReq(
            Integer userId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            List<LogItemReq> items
    ) { }

    /** 单个运动动作请求体（可携带 works） */
    public record LogItemReq(
            Integer type,
            Integer tOrder,
            Float   tWeight,
            Integer num,
            Integer avgScore,             // 可为 null，服务端默认置 0
            List<LogWorkReq> works        // 可为 null/空
    ) { }

    /** 单个动作得分请求体 */
    public record LogWorkReq(
            Integer acOrder,
            Integer score                 // 可为 null，服务端默认置 0
    ) { }

    /** 返回给前端的完整训练记录（保持原有结构：session + items） */
    public record LogDayResp(
            LogSession session,
            List<LogItem> items
    ) {
        public static LogDayResp from(com.quark.app.service.LogSessionService.LogDayDto dto) {
            return new LogDayResp(dto.session(), dto.items());
        }
    }
}
