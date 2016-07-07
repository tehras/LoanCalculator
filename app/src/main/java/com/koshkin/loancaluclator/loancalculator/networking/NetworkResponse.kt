package com.koshkin.loancaluclator.loancalculator.networking

/**
 * Created by koshkin on 7/4/16.
 */
interface NetworkResponse {

    fun onResponse(response: Response, request: Request)

}
