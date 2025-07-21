package com.quark.app.service;

import com.quark.app.entity.PlanItem;
import com.quark.app.entity.PlanSession;
import com.quark.app.repository.PlanItemRepository;
import com.quark.app.repository.PlanSessionRepository;
import org.springframework.data.domain.Sort;           // ← 新增
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanSessionService {

    private final PlanSessionRepository sessionRepo;
    private final PlanItemRepository    itemRepo;

    public PlanSessionService(PlanSessionRepository sessionRepo,
                              PlanItemRepository itemRepo) {
        this.sessionRepo = sessionRepo;
        this.itemRepo    = itemRepo;
    }

    /**
     * 新建【某一天】的完整训练计划……
     */
    public PlanSession createDayPlan(Integer userId, LocalDate date, List<PlanItem> items) {
        sessionRepo.findByUserIdAndDate(userId, date).ifPresent(old -> {
            itemRepo.deleteBySessionSessionId(old.getSessionId());
            sessionRepo.delete(old);
        });

        PlanSession session = new PlanSession();
        session.setUserId(userId);
        session.setDate(date);
        session.setComplete(false);
        PlanSession savedSession = sessionRepo.save(session);

        for (PlanItem item : items) {
            item.setSession(savedSession);
        }
        itemRepo.saveAll(items);

        return savedSession;
    }

    /**
     * 按【用户 + 日期】查询完整训练计划（含动作列表）
     */
    @Transactional(readOnly = true)
    public Optional<PlanDayDto> getDayPlan(Integer userId, LocalDate date) {
        return sessionRepo.findByUserIdAndDate(userId, date)
                .map(session -> new PlanDayDto(
                    session,
                    // 这里传入 Sort.by("tOrder") 来保证按执行顺序排序
                    itemRepo.findBySessionSessionId(
                        session.getSessionId(),
                        Sort.by(Sort.Direction.ASC, "tOrder")
                    )
                ));
    }

    /* ====== 简易 DTO ====== */
    public record PlanDayDto(PlanSession session, List<PlanItem> items) {}
}
