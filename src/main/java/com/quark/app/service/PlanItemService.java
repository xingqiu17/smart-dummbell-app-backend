package com.quark.app.service;

import com.quark.app.entity.PlanItem;
import com.quark.app.repository.PlanItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlanItemService {

    private final PlanItemRepository itemRepo;

    public PlanItemService(PlanItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    /** 使用 Sort.by("tOrder") 按执行顺序排序 */
    public List<PlanItem> listItemsBySession(Integer sessionId) {
        return itemRepo.findBySessionSessionId(
            sessionId,
            Sort.by(Sort.Direction.ASC, "tOrder")
        );
    }
}
