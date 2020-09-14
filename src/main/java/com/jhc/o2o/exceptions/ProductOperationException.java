package com.jhc.o2o.exceptions;

public class ProductOperationException extends RuntimeException{

    private static final long serialVersionUID = 6197127863753651195L;

    public ProductOperationException(String msg){
        super(msg);
    }

}
