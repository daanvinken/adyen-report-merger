package com.adyen.reportMerger.entities;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a String list with the headers of all downloaded files, it should be a singleton
 */
public class HeaderCollection {

    private static HeaderCollection instance;
    private static List<String> headers;

    private HeaderCollection(){

    }

    public static HeaderCollection getInstance() {
        if (instance == null) {
            instance =  new HeaderCollection();
        }
        return instance;
    }

    public List<String> getHeaders(){
        return headers;
    }

    public void setHeaders(List<String> h) {
        clearHeaders();
        headers.addAll(h);
    }

    private void clearHeaders(){
        headers = new ArrayList<>();
    }

    public boolean isLastHeader(String s){
        if (headers == null || headers.size() == 0) {
            return  false;
        }

        int size = headers.size();
        return Objects.equal(headers.get(size-1), s);
    }

    public String getHeadersAsCsvLine(){
        StringBuilder sb = new StringBuilder();
        for (String s : headers) {
            sb.append(s);
            sb.append(",");

        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
