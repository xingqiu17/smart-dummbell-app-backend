// com/quark/app/dto/SessionDto.java
package com.quark.app.dto;

import java.time.LocalDate;

public class LogSessionDto {
    private Integer recordId;
    private Integer userId;
    private LocalDate date;
    private Integer hra;
    // 构造器、getter、setter
    public LogSessionDto(Integer recordId, Integer userId, LocalDate date, Integer hra) {
        this.recordId = recordId;
        this.userId   = userId;
        this.date     = date;
        this.hra      = hra;
    }
    // … getters & setters …
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
    public Integer getHra() {
        return hra;
    }
    public void setHra(Integer hra) {
        this.hra = hra;
    }
}
