package cn.tedu.csmall.passport.web;

import cn.tedu.csmall.passport.ex.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResult<T> implements Serializable {

    /**
     * 状态码
     */
    @ApiModelProperty("响应状态码")
    private Integer state;
    /**
     * 请求失败时的描述
     */
    @ApiModelProperty("响应状态描述")
    private String message;

    /**
     * 操作成功时的响应数据
     */
    @ApiModelProperty("响应数据")
    private T data;

    public JsonResult() {
    }

    public JsonResult(Integer state, String message) {
        this.state = state;
        this.message = message;
    }

    public static JsonResult<Void> ok(){
//        JsonResult jsonResult=new JsonResult();
//        jsonResult.setState(ServiceCode.OK.getValue());
//        return jsonResult;
        return ok(null);
    }

    public static <T> JsonResult<T> ok(T data){
        JsonResult jsonResult=new JsonResult();
        jsonResult.setState(ServiceCode.OK.getValue());
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult<Void> fail(ServiceCode state,String message){
        return new JsonResult(state.getValue(),message);
    }

    public static JsonResult<Void> fail(ServiceException e){
//        return new JsonResult(e.getServiceCode().getValue(),e.getMessage());
        return fail(e.getServiceCode(),e.getMessage());
    }

}
