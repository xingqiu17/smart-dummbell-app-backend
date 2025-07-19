package com.quark.app.service;

import com.quark.app.entity.Log;
import com.quark.app.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LogService {

    private final LogRepository repo;

    public LogService(LogRepository repo) {
        this.repo = repo;
    }

    /** 新增一条训练记录 */
    public Log addLog(Log log) {
        return repo.save(log);
    }

    /** 根据主键查询训练记录详情 */
    public Log detail(Integer groupId) {
        return repo.findById(groupId)
                   .orElseThrow(() -> new IllegalArgumentException("训练记录不存在"));
    }

    /** 查询某用户所有训练记录 */
    public List<Log> listByUser(Integer userId) {
        return repo.findByUserId(userId);
    }

    /** 查询某用户指定动作类型的训练记录 */
    public List<Log> listByUserAndType(Integer userId, Integer type) {
        return repo.findByUserIdAndType(userId, type);
    }

    /** 查询某用户在给定时间区间内的训练记录 */
    public List<Log> listByUserAndPeriod(Integer userId, LocalDateTime start, LocalDateTime end) {
        return repo.findByUserIdAndTTimeBetween(userId, start, end);
    }

    /** 更新训练记录 */
    public Log updateLog(Log log) {
        if (!repo.existsById(log.getGroupId())) {
            throw new IllegalArgumentException("训练记录不存在");
        }
        return repo.save(log);
    }

    /** 删除训练记录 */
    public void deleteLog(Integer groupId) {
        if (!repo.existsById(groupId)) {
            throw new IllegalArgumentException("训练记录不存在");
        }
        repo.deleteById(groupId);
    }
}
