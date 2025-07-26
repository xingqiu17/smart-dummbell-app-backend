package com.quark.app.service;

import com.quark.app.entity.PlanItem;
import com.quark.app.entity.PlanSession;
import com.quark.app.repository.PlanItemRepository;
import com.quark.app.repository.PlanSessionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional  // 默认所有方法都在事务中
public class PlanSessionService {

    private final PlanSessionRepository sessionRepo;
    private final PlanItemService         itemService;

    public PlanSessionService(PlanSessionRepository sessionRepo,
                              PlanItemService itemService) {
        this.sessionRepo = sessionRepo;
        this.itemService = itemService;
    }

    /**
     * 新建【某一天】的完整训练计划，不再覆盖旧记录
     */
    public PlanSession createDayPlan(Integer userId, LocalDate date, List<PlanItem> items) {
        PlanSession session = new PlanSession();
        session.setUserId(userId);
        session.setDate(date);
        session.setComplete(false);
        PlanSession savedSession = sessionRepo.save(session);

        items.forEach(item -> item.setSession(savedSession));
        itemService.saveAll(items);
        return savedSession;
    }

    /**
     * 查询某用户某日的所有完整训练计划（含动作列表）
     */
    @Transactional(readOnly = true)
    public List<PlanDayDto> getDayPlans(Integer userId, LocalDate date) {
        return sessionRepo
            .findAllByUserIdAndDate(userId, date)
            .stream()
            .map(session -> new PlanDayDto(
                session,
                itemService.listItemsBySession(session.getSessionId())
            ))
            .collect(Collectors.toList());
    }

    /**
     * ① 完整更新指定计划头（userId/date）和明细（删除旧明细后批量保存新明细）
     */
    public void updateSession(Integer sessionId, Integer newUserId, LocalDate newDate) {
        PlanSession session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("计划未找到: " + sessionId));
        session.setUserId(newUserId);
        session.setDate(newDate);
        sessionRepo.save(session);
    }

    public void updateItems(Integer sessionId, List<PlanItem> newItems) {
        // 1) 删除旧的
        itemService.deleteBySessionId(sessionId);
        // 2) 关联 session 并保存
        PlanSession session = findById(sessionId);
        newItems.forEach(i -> i.setSession(session));
        itemService.saveAll(newItems);
    }

    /**
     * ② 单独更新 complete 标志
     */
    public void updateCompleteFlag(Integer sessionId, Boolean complete) {
        PlanSession session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("计划未找到: " + sessionId));
        session.setComplete(complete);
        sessionRepo.save(session);
    }

    /**
     * 辅助：根据 ID 查询 PlanSession，找不到抛异常
     */
    @Transactional(readOnly = true)
    public PlanSession findById(Integer sessionId) {
        return sessionRepo.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("计划未找到: " + sessionId));
    }


    /**
     * 删除整个训练计划（先删明细，再删头）
     */
    public void deleteById(Integer sessionId) {
        // 删除该计划下所有动作明细
        itemService.deleteBySessionId(sessionId);
        // 删除计划头
        sessionRepo.deleteById(sessionId);
    }


    /* ====== 简易 DTO ====== */
    public record PlanDayDto(PlanSession session, List<PlanItem> items) {}
}
