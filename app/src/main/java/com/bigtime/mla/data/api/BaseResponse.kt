package com.bigtime.mla.data.api
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("data")
    val data: T?


)




{


    fun isSuccess():Boolean{

        return status
    }

}