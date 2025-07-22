package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "log_session")
public class LogSession implements Serializable {

    /** 主键：record_id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false, updatable = false)
    private Integer recordId;

    /** 用户 ID */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** 计划日期 */
    @Column(name = "date", nullable = false)
    private LocalDate date;



    /* ===== Getter / Setter ===== */

    public Integer getRecordId() {
        return recordId;
    }
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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


    @Override
    public String toString() {
        return "LogSession{" +
               "recordId=" + recordId +
               ", userId=" + userId +
               ", date=" + date +
               '}';
    }
}
