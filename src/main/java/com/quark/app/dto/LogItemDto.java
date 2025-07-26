// com/quark/app/dto/ItemDto.java
package com.quark.app.dto;

public class LogItemDto {
    private Integer groupId;
    private Integer type;
    private Integer number;
    private Integer tOrder;
    private Float tWeight;
    private Integer tAvgScore;
    // 构造器、getter、setter
    public LogItemDto(Integer groupId, Integer type, Integer number,
                   Integer torder, Float tweight, Integer tavgScore) {
        this.groupId   = groupId;
        this.type      = type;
        this.number    = number;
        this.tOrder    = torder;
        this.tWeight   = tweight;
        this.tAvgScore = tavgScore;
    }
    // … getters & setters …
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
    public Float getWeight() {
        return tWeight;
    }
    public void setWeight(Float tWeight) {
        this.tWeight = tWeight;
    }
    public Integer getAvgScore() {
        return tAvgScore;
    }
    public void setTAvgScore(Integer tAvgScore) {
        this.tAvgScore = tAvgScore;
    }
}
