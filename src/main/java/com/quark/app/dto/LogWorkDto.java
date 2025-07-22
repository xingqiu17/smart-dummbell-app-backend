// com/quark/app/dto/WorkDto.java
package com.quark.app.dto;

public class LogWorkDto {
    private Integer actionId;
    private Integer groupId;
    private Integer acOrder;
    private Integer score;
    // 构造器、getter、setter
    public LogWorkDto(Integer actionId, Integer groupId, Integer acOrder, Integer score) {
        this.actionId = actionId;
        this.groupId  = groupId;
        this.acOrder  = acOrder;
        this.score    = score;
    }
    // … getters & setters …
    public Integer getActionId() {
        return actionId;
    }
    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public Integer getAcOrder() {
        return acOrder;
    }
    public void setAcOrder(Integer acOrder) {
        this.acOrder = acOrder;
    }
    public Integer getScore() {
        return score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }
}
