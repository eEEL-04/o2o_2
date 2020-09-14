package com.jhc.o2o.enums;

public enum LocalAuthStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"内部系统错误"),
    NULL_AUTH_INFO(-1002,"空值错误"),
    ONLY_ONE_COUNT(-1003,"账号已存在");


    private int state;
    private String stateInfo;

    private LocalAuthStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     */
    public static LocalAuthStateEnum stateOf(int state){
        for (LocalAuthStateEnum stateEnum:values()){
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
