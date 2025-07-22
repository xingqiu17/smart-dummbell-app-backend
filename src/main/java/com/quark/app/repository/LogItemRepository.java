package com.quark.app.repository;

import com.quark.app.entity.LogItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogItemRepository extends JpaRepository<LogItem, Integer> {

    /** 根据 record_id 查询该训练记录下的所有运动组，按 t_order 排序 */
    List<LogItem> findByRecord_RecordId(Integer recordId, Sort sort);

    /** 删除某次训练记录下的所有运动组 */
    void deleteByRecord_RecordId(Integer recordId);
}
