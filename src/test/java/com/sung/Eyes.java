package com.sung;

import com.sung.json.scheam.generate.annotation.JsonStringSchema;

/**
 * Created by sungang on 2016/1/4.10:14
 */
public class Eyes {

    @JsonStringSchema
    private String eys;

    public String getEys() {
        return eys;
    }

    public void setEys(String eys) {
        this.eys = eys;
    }
}
