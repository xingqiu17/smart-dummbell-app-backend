package com.quark.app.repository;

import com.quark.app.entity.PlanItem;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItem, Integer> {

    /** 根据 session_id 查询该计划下的全部动作，按顺序字段排序 */
    List<PlanItem> findBySessionSessionId(Integer sessionId, Sort sort);

    /** 删除某个计划下的全部动作（更新计划时用得到） */
    void deleteBySessionSessionId(Integer sessionId);
}
