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

package com.bigtime.mla.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bigtime.mla.data.model.Program

/**
 * Interface for database access on Repo related operations.
 */

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

@Dao
abstract class UMSDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(vararg users:Program)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
      abstract fun insertUsers( users: List<Program>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
      abstract fun insertUser( users: Program)

      @Query(" SELECT * FROM Program ORDER BY event_timestamp ASC")
      abstract fun loadUsers(): LiveData<List<Program>>

    @Query(" DELETE FROM Program WHERE id = :id")
    abstract fun deleteEvent(id: Integer?)

    /*fun loadOrdered(repoIds: List<Int>): LiveData<List<Repo>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(repoIds)) { repositories ->
            Collections.sort(repositories) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            repositories
        }
    }*/

}
