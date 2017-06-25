package br.com.viradatecnologica.kotlinho.java;

import feign.Param;
import feign.RequestLine;

/**
 * Created by jyoshiriro on 6/25/17.
 */

public interface UserRequets {

    @RequestLine("GET /users/{id}")
    User getUser(@Param("id") Integer id);

}
