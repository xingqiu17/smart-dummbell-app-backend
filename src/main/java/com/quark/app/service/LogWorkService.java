package com.quark.app.service;

import com.quark.app.entity.LogWork;
import com.quark.app.repository.LogWorkRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogWorkService {

    private final LogWorkRepository workRepo;

    public LogWorkService(LogWorkRepository workRepo) {
        this.workRepo = workRepo;
    }

    /**
     * 查询某个运动（`LogItem`）下的所有动作（`LogWork`），按动作序号排序（ac_order）
     * 
     * @param groupId 运动组 ID
     * @return 该运动下的所有动作（`LogWork`）
     */
    public List<LogWork> getWorksByGroupId(Integer groupId) {
        return workRepo.findByGroup_GroupId(groupId, Sort.by(Sort.Direction.ASC, "acOrder"));
    }
}
