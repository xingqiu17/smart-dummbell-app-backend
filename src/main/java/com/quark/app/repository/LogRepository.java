package com.quark.app.repository;

import com.quark.app.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Log 实体的 DAO 接口
 */
public interface LogRepository extends JpaRepository<Log, Integer> {

    /** 查询某用户的所有训练记录 */
    List<Log> findByUserId(Integer userId);

    /** 查询某用户、某类型动作的训练记录 */
    List<Log> findByUserIdAndType(Integer userId, Integer type);

    /** 查询某用户在指定时间区间内的训练记录 */
    List<Log> findByUserIdAndTTimeBetween(Integer userId, LocalDateTime start, LocalDateTime end);
}
