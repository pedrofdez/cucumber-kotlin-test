package com.realxdata

import io.restassured.specification.RequestSpecification

interface RestAssuredSupport {

    fun RequestSpecification.When(): RequestSpecification {
        return this.`when`()
    }
}
