package br.com.viradatecnologica.kotlinho.java;

import feign.Param;
import feign.RequestLine;

public interface UserRequets {

    @RequestLine("GET /users/{id}")
    User getUser(@Param("id") Integer id);

}
