package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_item")
public class LogItem implements Serializable {

        /** 主键：group_id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false, updatable = false)
    private Integer groupId;

    /** 关联的计划头 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private LogSession record;

    /** 练习类型 */
    @Column(name = "type", nullable = false)
    private Integer type;


    /** 执行顺序 */
    @Column(name = "t_order", nullable = false)
    private Integer tOrder;

    /** 练习重量 */
    @Column(name = "t_weight", nullable = false)
    private Float tWeight;

    
    /** 练习次数 */
    @Column(name = "num", nullable = false)
    private Integer num;

    /** 练习平均分 */
    @Column(name = "avg_score", nullable = false)
    private Integer avgScore;


    /* ===== Getter / Setter ===== */

    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public LogSession getRecord() {
        return record;
    }
    public void setRecord(LogSession record) {
        this.record = record;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNumber() {
        return num;
    }
    public void setNumber(Integer num) {
        this.num = num;
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

        public Integer getTAvgScore() {
        return avgScore;
    }
    public void setTAvgScore(Integer avgScore) {
        this.avgScore = avgScore;
    }

    @Override
    public String toString() {
        return "LogItem{" +
               "groupId=" + groupId +
               ", recordId=" + (record != null ? record.getRecordId() : null) +
               ", type=" + type +
               ", num=" + num +
               ", tOrder=" + tOrder +
               ", tWeight=" + tWeight +
               ", avgScore=" + avgScore +
               '}';
    }
}
