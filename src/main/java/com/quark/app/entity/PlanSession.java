package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "plan_session")
public class PlanSession implements Serializable {

    /** 主键：session_id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", nullable = false, updatable = false)
    private Integer sessionId;

    /** 用户 ID */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** 计划日期 */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /** 完成标志：0=未完成 1=已完成 */
    @Column(name = "complete", nullable = false)
    private Boolean complete = false;

    /* ===== Getter / Setter ===== */

    public Integer getSessionId() {
        return sessionId;
    }
    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getComplete() {
        return complete;
    }
    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "PlanSession{" +
               "sessionId=" + sessionId +
               ", userId=" + userId +
               ", date=" + date +
               ", complete=" + complete +
               '}';
    }
}
