package com.sung;

import com.sung.json.scheam.generate.annotation.JsonStringSchema;

/**
 * Created by sungang on 2016/1/4.10:04
 */
public class Address {

    @JsonStringSchema
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
