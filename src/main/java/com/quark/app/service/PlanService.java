package com.quark.app.service;

import com.quark.app.entity.Plan;
import com.quark.app.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PlanService {

    private final PlanRepository repo;

    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    /** 新增一条计划 */
    public Plan addPlan(Plan plan) {
        // 如果需要防重复，可在此处加 exists 校验
        return repo.save(plan);
    }

    /** 根据主键查询计划详情 */
    public Plan detail(Integer planId) {
        return repo.findById(planId)
                   .orElseThrow(() -> new IllegalArgumentException("计划不存在"));
    }

    /** 查询某用户所有计划 */
    public List<Plan> listByUser(Integer userId) {
        return repo.findByUserId(userId);
    }

    /** 查询某用户指定日期的所有计划 */
    public List<Plan> listByUserAndDate(Integer userId, LocalDate date) {
        return repo.findByUserIdAndDate(userId, date);
    }

    /** 更新计划 */
    public Plan updatePlan(Plan plan) {
        if (!repo.existsById(plan.getPlanId())) {
            throw new IllegalArgumentException("计划不存在");
        }
        return repo.save(plan);
    }

    /** 删除计划 */
    public void deletePlan(Integer planId) {
        if (!repo.existsById(planId)) {
            throw new IllegalArgumentException("计划不存在");
        }
        repo.deleteById(planId);
    }
}
