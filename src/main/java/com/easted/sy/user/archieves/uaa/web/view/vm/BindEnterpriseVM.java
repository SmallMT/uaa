package com.easted.sy.user.archieves.uaa.web.view.vm;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BindEnterpriseVM {

//    @Max(value = 18,message = "统一社会心信用代码不能少于18位")
//    @Min(value = 18,message = "统一社会心信用代码不能大于18位")
    @NotNull(message = "统一社会信用代码不能为空")
    private String creditCode;


    @NotNull(message = "企业名称不能为空")
    private String enterpriseName;

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }


    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}
