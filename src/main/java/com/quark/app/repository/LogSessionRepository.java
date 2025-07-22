package com.quark.app.repository;

import com.quark.app.entity.LogSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogSessionRepository extends JpaRepository<LogSession, Integer> {

    /** 查询某用户所有训练记录（按日期倒序） */
    List<LogSession> findByUserIdOrderByDateDesc(Integer userId);

    /** 查询某用户在指定日期的所有训练记录 */
    List<LogSession> findByUserIdAndDate(Integer userId, LocalDate date);

    /** 查询某用户在一段时间内的训练记录（按日期倒序） */
    List<LogSession> findByUserIdAndDateBetweenOrderByDateDesc(
            Integer userId, LocalDate startDate, LocalDate endDate);
}
