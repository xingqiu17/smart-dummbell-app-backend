package com.quark.app.service;

import com.quark.app.entity.LogWork;
import com.quark.app.repository.LogWorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LogWorkService {

    private final LogWorkRepository repo;

    public LogWorkService(LogWorkRepository repo) {
        this.repo = repo;
    }

    /** 新增一次动作评分记录 */
    public LogWork addLogWork(LogWork logWork) {
        return repo.save(logWork);
    }

    /** 根据主键查询动作评分明细 */
    public LogWork detail(Integer id) {
        return repo.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("动作评分记录不存在"));
    }

    /** 查询某次训练下的所有评分明细 */
    public List<LogWork> listByGroup(Integer groupId) {
        return repo.findByGroupId(groupId);
    }

    /** 更新动作评分记录 */
    public LogWork updateLogWork(LogWork logWork) {
        if (!repo.existsById(logWork.getId())) {
            throw new IllegalArgumentException("动作评分记录不存在");
        }
        return repo.save(logWork);
    }

    /** 删除动作评分记录 */
    public void deleteLogWork(Integer id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("动作评分记录不存在");
        }
        repo.deleteById(id);
    }
}
