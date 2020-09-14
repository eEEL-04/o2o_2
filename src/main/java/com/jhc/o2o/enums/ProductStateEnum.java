package com.jhc.o2o.enums;

public enum ProductStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"内部系统错误"),
    EMPTY(-1002,"商品信息为空");

    private int state;
    private String stateInfo;

    private ProductStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     */
    public static ProductStateEnum stateOf(int state){
        for (ProductStateEnum stateEnum:values()){
            if (stateEnum.getState() == state){
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
