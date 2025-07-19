package com.quark.app.repository;

import com.quark.app.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Plan 实体的 DAO 接口
 */
public interface PlanRepository extends JpaRepository<Plan, Integer> {

    /** 根据用户 ID 查询所有计划 */
    List<Plan> findByUserId(Integer userId);

    /** 根据用户 ID 和日期查询当天的所有计划 */
    List<Plan> findByUserIdAndDate(Integer userId, LocalDate date);

    /** 判断某个用户在指定日期是否已有计划 */
    boolean existsByUserIdAndDate(Integer userId, LocalDate date);
}
