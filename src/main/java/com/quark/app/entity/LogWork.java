package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 训练动作评分明细表实体 —— log_work
 */
@Entity
@Table(name = "log_work")
public class LogWork implements Serializable {
        /** 主键：item_id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id", nullable = false, updatable = false)
    private Integer actionId;

    /** 关联的计划头 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private LogItem group;




    /** 执行顺序 */
    @Column(name = "ac_order", nullable = false)
    private Integer acOrder;

    /** 练习得分 */
    @Column(name = "score", nullable = false)
    private Integer score;


    /* ===== Getter / Setter ===== */

    public Integer getActionId() {
        return actionId;
    }
    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }


    public LogItem getGroup() {
        return group;
    }

    public void setGroup(LogItem group) {
        this.group = group;
    }




    public Integer getTAcOrder() {
        return acOrder;
    }
    public void setTAcOrder(Integer acOrder) {
        this.acOrder = acOrder;
    }

    public Integer getScore() {
        return score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }



    @Override
    public String toString() {
        return "LogWork{" +
               "actionId=" + actionId +
               ", groupId=" + (group != null ? group.getGroupId() : null) +
               ", acOrder=" + acOrder +
               ", score=" + score +
               '}';
    }
}

