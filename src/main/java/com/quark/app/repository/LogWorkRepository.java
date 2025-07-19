package com.quark.app.repository;

import com.quark.app.entity.LogWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * LogWork 实体的 DAO 接口
 */
public interface LogWorkRepository extends JpaRepository<LogWork, Integer> {

    /** 查询某次训练下的所有动作评分明细 */
    List<LogWork> findByGroupId(Integer groupId);
}
