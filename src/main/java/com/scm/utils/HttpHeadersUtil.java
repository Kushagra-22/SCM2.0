package com.scm.utils;

import javax.print.attribute.standard.Media;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import com.fasterxml.jackson.databind.JsonSerializable.Base;



public class HttpHeadersUtil {
    // public HttpHeaders getHeaders() {
    //     HttpHeaders headers = new HttpHeaders();
    //     String basicAuth=applicationId +":"+password;
    //     headers.add("Content_TYPE", MediaType.APPLICATION_JSON_VALUE);
    //     headers.add("Authorization", "Basic " + new String(Base64.encodeBase64(basicAuth.getBytes(),false)));
    //     return headers;
    // }
}
