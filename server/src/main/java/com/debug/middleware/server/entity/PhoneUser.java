package com.debug.middleware.server.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月19日 11:03
 */
@Data
@ToString
public class PhoneUser implements Serializable {
    private String phone;
    private Double fare;

    public PhoneUser() {
    }

    public PhoneUser(String phone, Double fare) {
        this.phone = phone;
        this.fare = fare;
    }

    //若手机号相同，代表充值重复，所以需要重写equals()和hasCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneUser phoneUser = (PhoneUser) o;
        return Objects.equals(phone, phoneUser.phone) && Objects.equals(fare, phoneUser.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, fare);
    }
}
