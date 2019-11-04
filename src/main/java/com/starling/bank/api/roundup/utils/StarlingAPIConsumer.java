package com.starling.bank.api.roundup.utils;

import com.starling.bank.api.roundup.pojos.Pojo;
import com.starling.bank.api.roundup.pojos.TransferUidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;


public class StarlingAPIConsumer {

    protected static final Logger log = LoggerFactory.getLogger(StarlingAPIConsumer.class);
    
    protected String uri;

    protected RestTemplate restTemplate;

    protected static final String bearer = "eyJhbGciOiJQUzI1NiJ9.eyJpc3MiOiJhcGktZGVtby5zdGFybGluZ2JhbmsuY29tIiwic3ViIjoiM2M0YTZkNDctZGUxZi00NWQzLWIzMTEtNTU1OTNiYzIzMWM2IiwiZXhwIjoxNTcyNzk0MDYyLCJpYXQiOjE1NzI3MDc2NjIsInNjb3BlIjoiYWNjb3VudC1ob2xkZXItbmFtZTpyZWFkIGFjY291bnQtaG9sZGVyLXR5cGU6cmVhZCBhY2NvdW50LWlkZW50aWZpZXI6cmVhZCBhY2NvdW50LWxpc3Q6cmVhZCBhY2NvdW50OnJlYWQgYWRkcmVzczplZGl0IGFkZHJlc3M6cmVhZCBhdHRhY2htZW50OnJlYWQgYXR0YWNobWVudDp3cml0ZSBhdXRob3Jpc2luZy1pbmRpdmlkdWFsOnJlYWQgYmFsYW5jZTpyZWFkIGNhcmQtY29udHJvbDplZGl0IGNhcmQ6cmVhZCBjb25maXJtYXRpb24tb2YtZnVuZHM6cmVhZCBjdXN0b21lcjpyZWFkIGVtYWlsOmVkaXQgbWFuZGF0ZTpkZWxldGUgbWFuZGF0ZTpyZWFkIG1lcmNoYW50OnJlYWQgbWV0YWRhdGE6Y3JlYXRlIG1ldGFkYXRhOmVkaXQgcGF5ZWU6Y3JlYXRlIHBheWVlOmRlbGV0ZSBwYXllZTplZGl0IHBheWVlLWltYWdlOnJlYWQgcGF5ZWU6cmVhZCBwYXktbG9jYWw6Y3JlYXRlIHBheS1sb2NhbC1vbmNlOmNyZWF0ZSBwYXktbG9jYWw6cmVhZCByZWNlaXB0OnJlYWQgcmVjZWlwdHM6cmVhZCByZWNlaXB0OndyaXRlIHNhdmluZ3MtZ29hbDpjcmVhdGUgc2F2aW5ncy1nb2FsOmRlbGV0ZSBzYXZpbmdzLWdvYWw6cmVhZCBzYXZpbmdzLWdvYWwtdHJhbnNmZXI6Y3JlYXRlIHNhdmluZ3MtZ29hbC10cmFuc2ZlcjpkZWxldGUgc2F2aW5ncy1nb2FsLXRyYW5zZmVyOnJlYWQgc3RhbmRpbmctb3JkZXI6Y3JlYXRlIHN0YW5kaW5nLW9yZGVyOmRlbGV0ZSBzdGFuZGluZy1vcmRlcjpyZWFkIHN0YXRlbWVudC1jc3Y6cmVhZCBzdGF0ZW1lbnQtcGRmOnJlYWQgdHJhbnNhY3Rpb246ZWRpdCB0cmFuc2FjdGlvbjpyZWFkIiwiYWNjb3VudEhvbGRlclVpZCI6IjRjYjIwY2M4LTczYTAtNDlhZS05YTliLWM2Yzg2M2YyODRhNCIsInRva2VuVWlkIjoiZjVkMmI5YTctZGJkZC00NmM2LWE2YmYtYzIxNTM4MzZjYTI4IiwiYXBpQWNjZXNzVWlkIjoiYWNiNWViYmQtZjI0Yi00ODE4LTk0MWQtNDllYmU5YjkwYjNjIn0.ZECpxGdPf182IyHkdvWwAEJBMEHDLnpj6L-M4nr8ppaPnBUItVBlZ3Y1C98P4_87sOSnq5511XBV2PPyAG51y3iDHJ0RB7TM9Uhm58FMrrJc6fkf6Mpm3pL0-nnXtIQWBdKppcMrnh0oeDbA9hdk4w6KflNhQomUeDouT26o60vAObw5_jFee53y_pM2ICjOB0w3RyKL1-PJPM10tKb8LUaPAF6guKJ3X0y7KtCZdAD1gNCL9qRlEV3xBwFcMZd27jJji1l9Dyyohx_1qJpmFBt1fVCQup0KDx57t0A3ksdwa3deMiHj4zTb0terggQN3mu9Z5C5R-Aqm25_nY3FR2kBJV6U2s1a_3bIf8Dpoqzy3p9SvxM133D5Wi7t24u0yxWbTmKz9uWhnR06A_PIdMCnyAuDVw0o6tURPv7C0MBO7d6ksesRUwYBUDdd68uAsRIPwt5OSC2nIXV_WimS4OCdyC_iGgxiJYhJ_YRe0noNqpNi1Qh7fu9sEVXFfjDyQl0aSnBzboDmr34tZJxM8n2cH53Z1GogHZ5M2IucerIWnXGy3lnv2F45m3WKr-uQkZhPA-GxTP84QWrcMnGVhuxHIH-KYd9PRuvhlxZnaNtbp54crkbxWuJBFpymfqv_A266BobhEM4mcZvK6MuGzuLbUEoqP_dNH9Br1K9616s";

    private HashMap<String, String>
            pathParameters = new HashMap<String, String>(),
            queryParameters = new HashMap<String, String>();

    protected StarlingAPIConsumer() {
        log.debug("StarlingAPIConsumer Default CTOR");
        restTemplate = new RestTemplate();
    }

    protected final HttpHeaders generateHeaders() {
        log.debug("generateHeaders");
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearer);

        return headers;
    }

    protected void addPathParameter(String name, String value) {
        // NB the order the parameters are added must be in line with the order they appear in the uri
        log.debug("addPathParameter [%s=%s]", name, value);
        pathParameters.put(name, value);
    }

    protected void addQueryParameter(String name, String value) {
        // NB the order the parameters are added must be in line with the order they appear in the query part of the uri
        log.debug("addqueryParameter [" + name + "=" + value + "]");
        queryParameters.put(name, value);
    }

    protected String processRequestQueryParameters() {
        log.debug("processRequestqueryParameters");
        StringBuilder url = new StringBuilder(this.uri);

        if( !queryParameters.isEmpty() ) {
            url.append("?");
            for (String name : queryParameters.keySet()) {
                String value = queryParameters.get(name);
                StringBuilder sb = new StringBuilder();
                sb.append(name).append("=").append(value).append("&");
                url.append(sb.toString());
            }
            url.deleteCharAt(url.length()-1); // remove the trailing &
        }

        return url.toString();
    }

    protected ResponseEntity<?> sendGetRequest(Pojo p) {
        log.debug("sendGetRequest");
        HttpEntity httpEntity = new HttpEntity(this.generateHeaders());
        return  restTemplate.exchange(processRequestQueryParameters(), HttpMethod.GET, httpEntity, p.getClass(), pathParameters);
    }

    protected ResponseEntity<?> sendCreatePutRequest(Pojo body, Pojo responseType) {
        log.debug("sendCreatePutRequest");
        HttpEntity httpEntity = new HttpEntity(body, this.generateHeaders());
        return restTemplate.exchange(processRequestQueryParameters(), HttpMethod.PUT, httpEntity, responseType.getClass(), pathParameters);
    }

    protected ResponseEntity sendTransferPutRequest(Pojo body) {
        log.debug("sendTransferPutRequest");
        HttpEntity<?> httpEntity = new HttpEntity<>(body, this.generateHeaders());
        return restTemplate.exchange(processRequestQueryParameters(), HttpMethod.PUT, httpEntity, TransferUidResponse.class, pathParameters);
    }

}
