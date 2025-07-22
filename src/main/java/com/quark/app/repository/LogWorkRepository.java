package com.quark.app.repository;

import com.quark.app.entity.LogWork;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogWorkRepository extends JpaRepository<LogWork, Integer> {

    /** 根据单个 groupId 查询并排序 */
    List<LogWork> findByGroup_GroupId(Integer groupId, Sort sort);

    /** 批量根据一组 groupId 查询，无排序 */
    List<LogWork> findByGroup_GroupIdIn(List<Integer> groupIds);

    /** 如果你也想要排序版本，可以这样写： */
    List<LogWork> findByGroup_GroupIdIn(List<Integer> groupIds, Sort sort);

    /** 删除某个动作组下的所有动作得分记录 */
    void deleteByGroup_GroupId(Integer groupId);
}
