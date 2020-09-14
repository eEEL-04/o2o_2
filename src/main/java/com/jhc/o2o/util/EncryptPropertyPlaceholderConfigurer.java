package com.jhc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    //需要加密的字段数组
    private String[] encryptPropNames = {"jdbc.username","jdbc.password"};

    /**
     * 对关键属性进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)){
            //对已加密字段进行解密工作
            String decryValue = DESUtil.getDecryptString(propertyValue);
            return decryValue;
        }else {
            return propertyValue;
        }
    }

    private boolean isEncryptProp(String propertyName) {
        //若等于需要加密的field,则进行加密
        for (String encryptPropName : encryptPropNames) {
            if (encryptPropName.equals(propertyName)){
                return true;
            }
        }
        return false;
    }


}
