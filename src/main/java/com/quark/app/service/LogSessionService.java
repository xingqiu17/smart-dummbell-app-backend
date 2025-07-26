package com.quark.app.service;

import com.quark.app.entity.LogItem;
import com.quark.app.entity.LogSession;
import com.quark.app.entity.LogWork;
import com.quark.app.repository.LogItemRepository;
import com.quark.app.repository.LogSessionRepository;
import com.quark.app.repository.LogWorkRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LogSessionService {

    private final LogSessionRepository sessionRepo;
    private final LogItemRepository    itemRepo;
    private final LogWorkRepository    workRepo;

    public LogSessionService(LogSessionRepository sessionRepo,
                             LogItemRepository itemRepo,
                             LogWorkRepository workRepo) {
        this.sessionRepo = sessionRepo;
        this.itemRepo    = itemRepo;
        this.workRepo    = workRepo;
    }

    /**
     * 【已有】创建【某一天】的单个训练记录（只落库 session + items，不包含 works）
     * - 补充：为了满足非空约束，这里为 LogItem.avgScore 赋默认值 0（若未传）
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

        // 2) 为这个 LogSession 绑定 LogItem（补默认值，绑定外键）
        if (items != null && !items.isEmpty()) {
            for (LogItem item : items) {
                item.setRecord(savedSession);             // 绑定外键（训练记录）
                if (item.getTAvgScore() == null) {        // 非空列，默认 0
                    item.setTAvgScore(0);
                }
            }
            itemRepo.saveAll(items);
        }

        return savedSession;
    }

    /**
     * 【新增】一次请求创建【session + items + works】（方案 A：显式保存，不依赖级联）
     * 说明：
     *  - itemsWithWorks 的顺序需与最终保存的 items 一一对应；
     *  - 为满足非空约束，LogItem.avgScore 与 LogWork.score 均默认 0（若未传）；
     *  - 全过程在一个事务中，任一步失败将回滚。
     *
     * @param userId         用户 ID
     * @param date           日期 (yyyy-MM-dd)
     * @param itemsWithWorks 每个 item 及其下属的 works
     * @return 新建的 LogSession（已带自增 record_id）
     */
    public LogSession createDayRecordWithWorks(
            Integer userId,
            LocalDate date,
            List<LogItemCreate> itemsWithWorks
    ) {
        // 1) 保存 session
        LogSession session = new LogSession();
        session.setUserId(userId);
        session.setDate(date);
        LogSession savedSession = sessionRepo.save(session);

        if (itemsWithWorks == null || itemsWithWorks.isEmpty()) {
            return savedSession; // 无明细直接返回
        }

        // 2) 组装并保存 items
        List<LogItem> itemsToSave = new ArrayList<>(itemsWithWorks.size());
        for (LogItemCreate ic : itemsWithWorks) {
            LogItem item = new LogItem();
            item.setRecord(savedSession);
            item.setType(ic.type());
            item.setNumber(ic.number());
            item.setTOrder(ic.tOrder());
            item.setTWeight(ic.tWeight());
            // 非空字段默认值
            Integer avgScore = ic.avgScore();
            item.setTAvgScore(avgScore != null ? avgScore : 0);
            itemsToSave.add(item);
        }
        List<LogItem> savedItems = itemRepo.saveAll(itemsToSave);

        // 3) 组装并保存 works（与 items 按索引对应）
        List<LogWork> worksToSave = new ArrayList<>();
        for (int i = 0; i < savedItems.size(); i++) {
            LogItem savedItem = savedItems.get(i);
            List<LogWorkCreate> works = itemsWithWorks.get(i).works();
            if (works == null || works.isEmpty()) continue;

            for (LogWorkCreate wc : works) {
                LogWork w = new LogWork();
                w.setGroup(savedItem);
                w.setTAcOrder(wc.acOrder());                     // 注意：实体方法名为 setTAcOrder
                Integer score = wc.score();
                w.setScore(score != null ? score : 0);           // 非空字段默认值
                worksToSave.add(w);
            }
        }
        if (!worksToSave.isEmpty()) {
            workRepo.saveAll(worksToSave);
        }

        return savedSession;
    }

    /**
     * 查询【用户 + 日期】的训练记录（多个 LogSession） + 每个 session 下的 items
     * （如需带上 works，可在调用处配合 LogWorkService 逐组查询）
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

    /* ====== 简易 DTO（查询用）====== */
    public record LogDayDto(LogSession session, List<LogItem> items) {}

    /* ====== 创建用最小 DTO（服务内定义；控制层可直接复用）====== */
    public record LogItemCreate(
            Integer type,
            Integer number,
            Integer tOrder,
            Float   tWeight,
            Integer avgScore,                 // 可为 null；服务端默认 0
            List<LogWorkCreate> works         // 可为 null / 空
    ) {}

    public record LogWorkCreate(
            Integer acOrder,
            Integer score                      // 可为 null；服务端默认 0
    ) {}
}
