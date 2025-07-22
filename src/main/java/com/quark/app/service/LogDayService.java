// com/quark/app/service/LogDayService.java
package com.quark.app.service;

import com.quark.app.dto.*;
import com.quark.app.entity.*;
import com.quark.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogDayService {

    @Autowired private LogSessionRepository sessionRepo;
    @Autowired private LogItemRepository    itemRepo;
    @Autowired private LogWorkRepository    workRepo;

    /**
     * 按 userId + date 返回当天的 session、items 列表 以及 works 的 Map（key = groupId）。
     */
    public LogDayDto getDayRecords(Integer userId, LocalDate date) {
        // 假设每天只有一个 session，取第一条；如果有多条，可改为 List<LogDayDto>
        LogSessionDto sessionDto = sessionRepo.findByUserIdAndDate(userId, date)
            .stream()
            .findFirst()
            .map(s -> new LogSessionDto(s.getRecordId(), s.getUserId(), s.getDate()))
            .orElse(null);

        if (sessionDto == null) {
            return new LogDayDto(null, Collections.emptyList(), Collections.emptyMap());
        }

        // 1. 查所有 items
        List<LogItemDto> items = itemRepo.findByRecord_RecordId(sessionDto.getRecordId(),Sort.unsorted())
            .stream()
            .map(item -> new LogItemDto(
                item.getGroupId(),
                item.getType(),
                item.getNumber(),
                item.getTOrder(),
                item.getTWeight(),
                item.getTAvgScore()
            ))
            .collect(Collectors.toList());

        // 2. 查所有 works 并分组
        List<LogWorkDto> allWorks = workRepo.findByGroup_GroupIdIn(
            items.stream().map(LogItemDto::getGroupId).collect(Collectors.toList())
        ).stream()
         .map(w -> new LogWorkDto(
             w.getActionId(),
             w.getGroup().getGroupId(),
             w.getTAcOrder(),
             w.getScore()
         ))
         .collect(Collectors.toList());

        Map<Integer, List<LogWorkDto>> worksMap = allWorks.stream()
            .collect(Collectors.groupingBy(LogWorkDto::getGroupId));

        return new LogDayDto(sessionDto, items, worksMap);
    }
}
