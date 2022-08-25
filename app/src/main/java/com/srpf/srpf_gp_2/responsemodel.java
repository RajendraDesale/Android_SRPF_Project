package com.srpf.srpf_gp_2;

public class responsemodel
{
    String message;

    public responsemodel(String message) {
        this.message = message;
    }

    public responsemodel() {
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
