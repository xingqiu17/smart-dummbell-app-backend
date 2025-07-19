package com.quark.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "user",
       indexes = { @Index(name = "idx_user_account", columnList = "account", unique = true) })
public class User implements Serializable {

    /** 主键：user_id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Integer userId;

    /** 硬件 ID：HW_id */
    @Column(name = "HW_id", nullable = false)
    private Integer hwId;

    /** 账号（登录名） */
    @Column(name = "account", length = 16, nullable = false, unique = true)
    private String account;

    /** 密码（建议后续做加盐加密） */
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    /** 昵称 / 真实姓名 */
    @Column(name = "name", length = 16, nullable = false)
    private String name;

    /** 性别：0=男 1=女 */
    @Column(name = "gender", nullable = false)
    private Integer gender;

    /** 身高（cm） */
    @Column(name = "height", nullable = false)
    private Float height;

    /** 体重（kg） */
    @Column(name = "weight", nullable = false)
    private Float weight;

    /** 出生日期 */
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    /** 训练目标（自定义枚举/数值） */
    @Column(name = "aim", nullable = false)
    private Integer aim;

    /** 硬件测得体重，可为空 */
    @Column(name = "HW_weight")
    private Float hwWeight;

    /* ==== Getter / Setter ==== */

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getHwId() { return hwId; }
    public void setHwId(Integer hwId) { this.hwId = hwId; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public Float getHeight() { return height; }
    public void setHeight(Float height) { this.height = height; }

    public Float getWeight() { return weight; }
    public void setWeight(Float weight) { this.weight = weight; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public Integer getAim() { return aim; }
    public void setAim(Integer aim) { this.aim = aim; }

    public Float getHwWeight() { return hwWeight; }
    public void setHwWeight(Float hwWeight) { this.hwWeight = hwWeight; }

    @Override
    public String toString() {
        return "User{" +
               "userId=" + userId +
               ", hwId=" + hwId +
               ", account='" + account + '\'' +
               ", name='" + name + '\'' +
               ", gender=" + gender +
               ", height=" + height +
               ", weight=" + weight +
               ", birthday=" + birthday +
               ", aim=" + aim +
               ", hwWeight=" + hwWeight +
               '}';
    }
}
