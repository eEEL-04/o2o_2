package com.jhc.o2o.exceptions;

public class ProductCategoryOperationException extends RuntimeException{

    private static final long serialVersionUID = 2267481555091960537L;

    public ProductCategoryOperationException(String msg){
        super(msg);
    }

}
