package org.x.cicada.response;

import lombok.Data;

@Data
public class SimpleResponse<T> {

    private int status;

    private String msg;

    private T data;
}
