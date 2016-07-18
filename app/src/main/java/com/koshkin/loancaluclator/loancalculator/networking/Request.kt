package com.koshkin.loancaluclator.loancalculator.networking

/**
 * Created by koshkin on 7/4/16.
 *
 * Request type
 */
class Request {

    constructor(requestType: Type, url: String, parsingObject: ParsingObject, callbackObj: NetworkResponse) {
        this.requestType = requestType
        this.url = url
        this.parsingObject = parsingObject
        this.networkResponse = callbackObj
    }

    lateinit var requestType: Type
    lateinit var parsingObject: ParsingObject
    lateinit var url: String
    lateinit var requestData: String
    lateinit var networkResponse: NetworkResponse

    enum class Type {
        GET, POST, PUT, DELETE
    }
}

val baseUrl: String = "http://loans-staging.koshkinbros.com/"
val extLoans: String = "loans"
val extLoan: String = "loans/{loan-id}"
val extPayments: String = "payments"
