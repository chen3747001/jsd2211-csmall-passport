package cn.tedu.csmall.passport.web;

/**
 * 状态码枚举类
 */
public enum ServiceCode {
    OK(20000),
    ERR_BAD_REQUEST(40000),
    ERR_NOT_FOUND(40400),
    ERR_CONFLICT(40900),
    ERR_INSERT(50000),
    ERR_DELETE(50100),
    ERR_UPDATE(50200),
    ERR_SELECT(50300);

    private Integer value;
    ServiceCode(Integer value) {
        this.value=value;
    }

    public Integer getValue(){
        return value;
    }
}
