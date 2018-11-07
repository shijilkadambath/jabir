/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigtime.mla.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import com.bigtime.mla.AppExecutors
import com.bigtime.mla.data.api.*
import com.bigtime.mla.data.db.AppDb
import com.bigtime.mla.data.db.UMSDao
import com.bigtime.mla.data.model.Program
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

/**
 * Repository that handles Repo instances.
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
class UMSRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: AppDb,
        private val umsoDao: UMSDao,
        private val webService: WebService
) {
    //ApiResponse<BaseResponse<List<Program>>>

    fun loadUsers(start:String,end:String): LiveData<Resource<BaseResponse<PaginationResponse<List<Program>>>>> {


        return object : NetworkBoundResource<BaseResponse<PaginationResponse<List<Program>>>, BaseResponse<PaginationResponse<List<Program>>>>(appExecutors) {
            override fun saveCallResult(item: BaseResponse<PaginationResponse<List<Program>>>) {

                if (item.isSuccess() && item.data!=null && item.data.data !=null) {
                    umsoDao.insertUsers(item.data.data)
                }
            }

            override fun shouldFetch(data: BaseResponse<PaginationResponse<List<Program>>> ?): Boolean {
               // return data == null || !data.isSuccess() ||data.data ==null|| data.data.isEmpty()
                return true
            }

            override fun loadFromDb(): LiveData<BaseResponse<PaginationResponse<List<Program>>>> {

                val result = MediatorLiveData<BaseResponse<PaginationResponse<List<Program>>>>()


               result.addSource(umsoDao.loadUsers(), Observer {
                        list->
                   /*var myList: MutableList<Program> = mutableListOf<Program>()
                   myList.add(list!!.get(0))
                   myList.add(list!!.get(0))
                   myList.add(list!!.get(0))
                   myList.add(list!!.get(0))*/
                   //     result.setValue(BaseResponse("",true,PaginationResponse(myList.toList())))
                   result.setValue(BaseResponse("",true,PaginationResponse(list)))

                })

                return  result
            }

            override fun createCall() = webService.loadUsers(start,end)//2018-11-10



        }.asLiveData()
    }


    fun deleteEvent(id: Integer?): LiveData<Resource<BaseResponse<Program>>> {


        return object : NetworkBoundResource<BaseResponse<Program>, BaseResponse<Program>>(appExecutors) {


            override fun loadFromDb(): LiveData<BaseResponse<Program>> {
                val result = MediatorLiveData<BaseResponse<Program>>()


                result.addSource(umsoDao.loadUsers(), {
                    list->

                    result.setValue(resultData)

                })

                return  result
            }


            override fun shouldFetch(data: BaseResponse<Program>?): Boolean {
                return true
            }


            override fun createCall() = webService.delete(id!!.toString())

            var resultData: BaseResponse<Program>?=null

            override fun saveCallResult(item: BaseResponse<Program>) {
                if (item.isSuccess()) {
                    umsoDao.deleteEvent(id)
                }
                resultData = item
            }


        }.asLiveData()
    }


    fun deleteEvent(program: Program?): LiveData<Resource<BaseResponse<Program>>> {


        return object : NetworkBoundResource<BaseResponse<Program>, BaseResponse<Program>>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<BaseResponse<Program>>> {
                if (program!!.id.equals(Integer(0))){
                    return webService.postEvent(program.title,program.description,program.contact_name,
                            program.contact_phone,program.location,program.type,program.event_timestamp,
                            program.status)

                }else{
                    return webService.update(program.id.toString(),program.title,program.description,program.contact_name,
                            program.contact_phone,program.location,program.type,program.event_timestamp,
                            program.status)
                }
            }


            override fun loadFromDb(): LiveData<BaseResponse<Program>> {
                val result = MediatorLiveData<BaseResponse<Program>>()


                result.addSource(umsoDao.loadUsers(), {
                    list->

                    result.setValue(resultData)

                })

                return  result
            }


            override fun shouldFetch(data: BaseResponse<Program>?): Boolean {
                return true
            }



            var resultData: BaseResponse<Program>?=null

            override fun saveCallResult(item: BaseResponse<Program>) {
                if (item.isSuccess()) {
                    umsoDao.insertUser(program!!)
                }
                resultData = item
            }


        }.asLiveData()
    }


}
