package com.quark.app.repository;

import com.quark.app.entity.PlanSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanSessionRepository extends JpaRepository<PlanSession, Integer> {

    /** 查询某用户所有训练计划（按日期倒序） */
    List<PlanSession> findByUserIdOrderByDateDesc(Integer userId);

    /** 查询某用户在指定日期的所有训练计划 */
    List<PlanSession> findAllByUserIdAndDate(Integer userId, LocalDate date);

    /** （保留）原来的单条查询，如果以后还需要覆盖逻辑可以用它 */
    Optional<PlanSession> findByUserIdAndDate(Integer userId, LocalDate date);

    /** 查询某用户在一段时间内的训练计划 */
    List<PlanSession> findByUserIdAndDateBetweenOrderByDateDesc(
            Integer userId, LocalDate startDate, LocalDate endDate);
}
