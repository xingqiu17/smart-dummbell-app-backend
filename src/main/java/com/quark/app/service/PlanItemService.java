package com.quark.app.service;

import com.quark.app.entity.PlanItem;
import com.quark.app.repository.PlanItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional  // 保存和删除都需要事务
public class PlanItemService {

    private final PlanItemRepository itemRepo;

    public PlanItemService(PlanItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    /** 使用 Sort.by("tOrder") 按执行顺序排序 */
    @Transactional(readOnly = true)
    public List<PlanItem> listItemsBySession(Integer sessionId) {
        return itemRepo.findBySessionSessionId(
            sessionId,
            Sort.by(Sort.Direction.ASC, "tOrder")
        );
    }

    /** 删除某个计划下的全部动作（更新计划时用得到） */
    public void deleteBySessionId(Integer sessionId) {
        itemRepo.deleteBySessionSessionId(sessionId);
    }

    /** 批量保存新的动作列表（完整更新时用得到） */
    public List<PlanItem> saveAll(List<PlanItem> items) {
        return itemRepo.saveAll(items);
    }
}
