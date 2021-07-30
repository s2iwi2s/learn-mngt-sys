package com.s2i.lms.service.dto;

import java.util.Arrays;

public class FileDto {
    private byte[] byteAry;
    private String base64;

    public byte[] getByteAry() {
        return byteAry;
    }

    public void setByteAry(byte[] byteAry) {
        this.byteAry = byteAry;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public String toString() {
        return "FileDto{" +
            "byteAry=" + Arrays.toString(byteAry) +
            ", base64='" + base64 + '\'' +
            '}';
    }
}
