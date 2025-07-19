package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 训练动作评分明细表实体 —— log_work
 */
@Entity
@Table(name = "log_work")
public class LogWork implements Serializable {

    /** 动作评分记录主键 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    /** 所属训练记录组 ID */
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    /** 本次动作评分 */
    @Column(name = "score", nullable = false)
    private Integer score;

    /* ==== Getter / Setter ==== */

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
               "id=" + id +
               ", groupId=" + groupId +
               ", score=" + score +
               '}';
    }
}
