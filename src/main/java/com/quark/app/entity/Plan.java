package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "plan")
public class Plan implements Serializable {

    /** 主键：plan_id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false, updatable = false)
    private Integer planId;

    /** 用户 ID */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** 计划日期 */
    @Column(name = "date", nullable = false)
    private LocalDate date;

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
    private Integer tWeight;

    /** 完成标志：0=未完成 1=已完成 */
    @Column(name = "compelete", nullable = false)
    private Boolean compelete;

    /* ==== Getter / Setter ==== */

    public Integer getPlanId() {
        return planId;
    }
    public void setPlanId(Integer planId) {
        this.planId = planId;
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

    public Integer getTWeight() {
        return tWeight;
    }
    public void setTWeight(Integer tWeight) {
        this.tWeight = tWeight;
    }

    public Boolean getCompelete() {
        return compelete;
    }
    public void setCompelete(Boolean compelete) {
        this.compelete = compelete;
    }

    @Override
    public String toString() {
        return "Plan{" +
               "planId=" + planId +
               ", userId=" + userId +
               ", date=" + date +
               ", type=" + type +
               ", number=" + number +
               ", tOrder=" + tOrder +
               ", tWeight=" + tWeight +
               ", compelete=" + compelete +
               '}';
    }
}
