// com/quark/app/controller/LogDayController.java
package com.quark.app.controller;

import com.quark.app.dto.LogDayDto;
import com.quark.app.service.LogDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/logs")
public class LogDayController {

    @Autowired
    private LogDayService logDayService;

    /**
     * 查询指定用户在某天的训练记录（Session + Items + WorksMap）
     *
     * 示例请求：
     *   GET /api/logs/day?userId=42&date=2025-07-22
     *
     * @param userId 用户 ID
     * @param date   查询日期，格式 yyyy-MM-dd
     */
    @GetMapping("/day")
    public LogDayDto getDayRecords(
            @RequestParam("userId") Integer userId,
            @RequestParam("date")   String date
    ) {
        LocalDate d = LocalDate.parse(date);
        return logDayService.getDayRecords(userId, d);
    }
}
