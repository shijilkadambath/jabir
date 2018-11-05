package com.bigtime.mla.ui.login
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.bigtime.mla.common.AbsentLiveData
import com.bigtime.mla.data.api.BaseResponse
import com.bigtime.mla.data.api.PaginationResponse
import com.bigtime.mla.data.api.Resource
import com.bigtime.mla.data.model.Program
import com.bigtime.mla.repo.UMSRepository
import javax.inject.Inject

class LoginViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {

    private val _login = MutableLiveData<String>()

    private val _delete = MutableLiveData<Integer>()

    private val _post = MutableLiveData<Program>()

    /*val login: LiveData<String>
        get() = _login*/

    val repositories: LiveData<Resource<BaseResponse<PaginationResponse<List<Program>>>>> = Transformations
            .switchMap(_login) { login ->
                if (login == null) {
                    AbsentLiveData.create()
                } else {
                    repoRepository.loadUsers()
                }
            }

    val deleteRepo: LiveData<Resource<BaseResponse<Program>>> = Transformations
            .switchMap(_delete) { login ->
                if (login == null) {
                    AbsentLiveData.create()
                } else {
                    repoRepository.deleteEvent(login)
                }
            }


    val postRepo: LiveData<Resource<BaseResponse<Program>>> = Transformations
            .switchMap(_post) { login ->
                if (login == null) {
                    AbsentLiveData.create()
                } else {
                    repoRepository.deleteEvent(login)
                }
            }


    fun retry() {
        _login.value?.let {
            _login.value = it
        }
    }

    fun loadData() {
        //if (_login.value != login) {
            _login.value = "test"
        //}
    }

    fun deleteEvent(id: Integer) {
        _delete.value = id;
    }

    fun postEvent(id: Program) {
        _post.value = id
    }
}