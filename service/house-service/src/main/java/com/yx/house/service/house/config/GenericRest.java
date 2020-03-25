package com.yx.house.service.house.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/03/15:09
 */
@Service
public class GenericRest {

    @Autowired
    private RestTemplate lbRestTemplate;

    @Autowired
    private RestTemplate directRestTemplate;

    private static final String directFlag = "direct://";

    public <T> ResponseEntity<T> post(String url, Object reqBody, ParameterizedTypeReference<T> reponseType){
        RestTemplate template = this.getRestTemplate(url);
        url = url.replace(directFlag,"");
        return template.exchange(url, HttpMethod.POST, new HttpEntity(reqBody),reponseType);
    }
    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> reponseType){
        RestTemplate template = this.getRestTemplate(url);
        url = url.replace(directFlag,"");
        return template.exchange(url, HttpMethod.GET, HttpEntity.EMPTY,reponseType);
    }

    private RestTemplate getRestTemplate(String url) {
        if(url.contains(directFlag)){
            return directRestTemplate;
        }else{
            return lbRestTemplate;
        }
    }

}
