package com.quark.app.service;

import com.quark.app.entity.LogItem;
import com.quark.app.repository.LogItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogItemService {

    private final LogItemRepository itemRepo;

    public LogItemService(LogItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    /**
     * 查询某个训练记录（`LogSession`）下的所有运动（`LogItem`），按顺序排序（t_order）
     * 
     * @param recordId 训练记录 ID
     * @return 该记录下的所有运动（`LogItem`）
     */
    public List<LogItem> getItemsBySessionId(Integer recordId) {
        return itemRepo.findByRecord_RecordId(recordId, Sort.by(Sort.Direction.ASC, "tOrder"));
    }
}
