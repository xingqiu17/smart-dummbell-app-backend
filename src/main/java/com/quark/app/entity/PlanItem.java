package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "plan_item")
public class PlanItem implements Serializable {

    /** 主键：item_id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false, updatable = false)
    private Integer itemId;

    /** 关联的计划头 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private PlanSession session;

    /** 练习类型 */
    @Column(name = "type", nullable = false)
    private Integer type;

    /** 练习次数 */
    @Column(name = "number", nullable = false)
    private Integer number;

    /** 执行顺序 */
    @Column(name = "t_order", nullable = false)
    private Integer tOrder;

    /** 练习重量 */
    @Column(name = "t_weight", nullable = false)
    private Float tWeight;

    /** 完成标志：0=未完成 1=已完成 */
    @Column(name = "complete", nullable = false)
    private Boolean complete = false;

    /* ===== Getter / Setter ===== */

    public Integer getItemId() {
        return itemId;
    }
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public PlanSession getSession() {
        return session;
    }
    public void setSession(PlanSession session) {
        this.session = session;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getTOrder() {
        return tOrder;
    }
    public void setTOrder(Integer tOrder) {
        this.tOrder = tOrder;
    }

    public Float getTWeight() {
        return tWeight;
    }
    public void setTWeight(Float tWeight) {
        this.tWeight = tWeight;
    }

    public Boolean getComplete() {
        return complete;
    }
    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "PlanItem{" +
               "itemId=" + itemId +
               ", sessionId=" + (session != null ? session.getSessionId() : null) +
               ", type=" + type +
               ", number=" + number +
               ", tOrder=" + tOrder +
               ", tWeight=" + tWeight +
               ", complete=" + complete +
               '}';
    }
}
