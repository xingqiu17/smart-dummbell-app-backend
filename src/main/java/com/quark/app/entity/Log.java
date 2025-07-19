package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "log")
public class Log implements Serializable {

    /** 训练记录主键 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false, updatable = false)
    private Integer groupId;

    /** 用户 ID */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** 训练动作类型 */
    @Column(name = "type", nullable = false)
    private Integer type;

    /** 动作顺序：第几次重复 */
    @Column(name = "t_order", nullable = false)
    private Integer tOrder;

    /** 哑铃配重 */
    @Column(name = "t_weight", nullable = false)
    private Integer tWeight;

    /** 训练开始时间 */
    @Column(name = "t_time", nullable = false)
    private LocalDateTime tTime;

    /** 此次动作次数 */
    @Column(name = "num", nullable = false)
    private Integer num;

    /** 该组动作的平均评分 */
    @Column(name = "avg_score", nullable = false)
    private Integer avgScore;

    /* ==== Getter / Setter ==== */

    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTOrder() {
        return tOrder;
    }
    public void setTOrder(Integer tOrder) {
        this.tOrder = tOrder;
    }

    public Integer getTWeight() {
        return tWeight;
    }
    public void setTWeight(Integer tWeight) {
        this.tWeight = tWeight;
    }

    public LocalDateTime getTTime() {
        return tTime;
    }
    public void setTTime(LocalDateTime tTime) {
        this.tTime = tTime;
    }

    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getAvgScore() {
        return avgScore;
    }
    public void setAvgScore(Integer avgScore) {
        this.avgScore = avgScore;
    }

    @Override
    public String toString() {
        return "Log{" +
               "groupId=" + groupId +
               ", userId=" + userId +
               ", type=" + type +
               ", tOrder=" + tOrder +
               ", tWeight=" + tWeight +
               ", tTime=" + tTime +
               ", num=" + num +
               ", avgScore=" + avgScore +
               '}';
    }
}
