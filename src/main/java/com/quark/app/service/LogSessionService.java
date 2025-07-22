package com.quark.app.service;

import com.quark.app.entity.LogSession;
import com.quark.app.entity.LogItem;
import com.quark.app.repository.LogSessionRepository;
import com.quark.app.repository.LogItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogSessionService {

    private final LogSessionRepository sessionRepo;
    private final LogItemRepository itemRepo;

    public LogSessionService(LogSessionRepository sessionRepo, LogItemRepository itemRepo) {
        this.sessionRepo = sessionRepo;
        this.itemRepo = itemRepo;
    }

    /**
     * 创建【某一天】的单个训练记录：
     * 1. 保存单个 LogSession 训练记录
     * 2. 为这个训练记录绑定多个 LogItem 动作
     *
     * @param userId 用户 ID
     * @param date   日期 (yyyy-MM-dd)
     * @param items  该日动作列表（按 t_order 排序）
     * @return 新建的 LogSession（已带自增 record_id）
     */
    public LogSession createDayRecord(Integer userId, LocalDate date, List<LogItem> items) {
        // 1) 保存单个 LogSession（代表一次训练会话）
        LogSession session = new LogSession();
        session.setUserId(userId);
        session.setDate(date);
        LogSession savedSession = sessionRepo.save(session);

        // 2) 为这个 LogSession 绑定 LogItem（使用 setRecord 方法）
        for (LogItem item : items) {
            item.setRecord(savedSession);  // 绑定外键（训练记录）
        }
        itemRepo.saveAll(items);

        return savedSession;
    }

    /**
     * 查询【用户 + 日期】的训练记录（多个 LogSession）
     *
     * @param userId 用户 ID
     * @param date   日期 (yyyy-MM-dd)
     * @return 当天的所有训练记录及其动作信息
     */
    @Transactional(readOnly = true)
    public List<LogDayDto> getDayRecords(Integer userId, LocalDate date) {
        return sessionRepo.findByUserIdAndDate(userId, date)
                .stream()
                .map(session -> new LogDayDto(
                        session,
                        itemRepo.findByRecord_RecordId(
                            session.getRecordId(),
                            Sort.by(Sort.Direction.ASC, "tOrder")
                        )
                ))
                .toList();
    }

    /* ====== 简易 DTO ====== */
    public record LogDayDto(LogSession session, List<LogItem> items) {}
}
