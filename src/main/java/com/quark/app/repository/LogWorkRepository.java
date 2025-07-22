package com.quark.app.repository;

import com.quark.app.entity.LogWork;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogWorkRepository extends JpaRepository<LogWork, Integer> {

    /** 根据 group_id 查询该动作组下的所有动作得分，按 ac_order 排序 */
    List<LogWork> findByGroup_GroupId(Integer groupId, Sort sort);

    /** 删除某个动作组下的所有动作得分记录 */
    void deleteByGroup_GroupId(Integer groupId);
}
