package com.bigtime.mla.ui.login

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bigtime.mla.AppExecutors

import com.bigtime.mla.R
import com.bigtime.mla.binding.FragmentDataBindingComponent
import com.bigtime.mla.common.autoCleared
import com.bigtime.mla.data.api.Status
import com.bigtime.mla.data.model.Program
import com.bigtime.mla.databinding.FragmentLoginBinding
import com.bigtime.mla.di.Injectable
import com.bigtime.mla.ui.BaseFragment
import com.bigtime.mla.ui.RetryCallback
import com.bigtime.mla.ui.picker.DatePickerFragment
import com.bigtime.mla.utils.TimeUtils
import com.bigtime.mla.widget.RecyclerSectionItemDecoration
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var mViewModel: LoginViewModel
    var dateFragment: DatePickerFragment? = null
    private var timeCalender: Calendar? = Calendar.getInstance()

    var adapter by autoCleared<ListAdapter>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_login;
    }
     //8606971903

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(LoginViewModel::class.java)


        val sectionItemDecoration = RecyclerSectionItemDecoration(resources.getDimensionPixelSize(R.dimen.edittext_hieght), true, getSectionCallback())
        mBinding.listUser.addItemDecoration(sectionItemDecoration)

        adapter = ListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors) {
            navController().navigate(
                    LoginFragmentDirections.showRegistration(it)
            )
        }

        mBinding.listUser.adapter = adapter
        //adapter = rvAdapter

        //mBinding.image = "https://cdn.freebiesupply.com/logos/large/2x/android-logo-png-transparent.png"

        mBinding.callback = object : RetryCallback {
            override fun retry() {
                mViewModel.retry()
            }
        }

        mViewModel.repositories.observe(this, Observer { result ->
            mBinding.searchResource = result

            if (result?.status != Status.LOADING){
                mBinding.swipeContainer.isRefreshing=false
            }

            if (result?.status == Status.ERROR){
                Toast.makeText(activity,result?.message,Toast.LENGTH_SHORT).show()
            }

            if(result?.data?.data !=null){



                mBinding.resultCount = result.data.data.data?.size ?: 0
                adapter.submitList(result.data.data.data)

            }
        })

        val temp: Integer = Integer(0)

        mBinding.fabAdd.setOnClickListener({
            navController().navigate(



                    LoginFragmentDirections.showAddEvent(Program("",temp,"","","",0,"","","","",""))
            )
        })

        mBinding.swipeContainer.setOnRefreshListener {

            mViewModel.loadData(timeCalender)
        };

        dateFragment = DatePickerFragment()


        dateFragment!!.setListener(object :  DatePickerFragment.OnDateSetListener{

            override fun onDateSet(y: Int, m: Int, d: Int){

                timeCalender!!.set(Calendar.YEAR,y)
                timeCalender!!.set(Calendar.DAY_OF_MONTH,d)
                timeCalender!!.set(Calendar.MONTH,m)


                val formatDate = SimpleDateFormat("MMMM dd yyyy")
                val formattedTime = formatDate.format(timeCalender!!.time).toString()

                //var x:String = String.format("%02d", d)+ "-" +String.format("%02d", m) + "-"+y;
                mBinding.txtDate.setText(formattedTime)

            }

        })
        mBinding.txtDate.setOnClickListener(View.OnClickListener {
            pickDate()
        })


        mViewModel.loadData(timeCalender)

        val formatDate = SimpleDateFormat("MMMM dd yyyy")
        val formattedTime = formatDate.format(timeCalender!!.time).toString()

        mBinding.txtDate.setText(formattedTime)
    }
    fun pickDate(){
        dateFragment!!.show(childFragmentManager, "timePicker")
    }

    fun navController() = findNavController()


    private fun getSectionCallback(): RecyclerSectionItemDecoration.SectionCallback {
        return object : RecyclerSectionItemDecoration.SectionCallback {
            override fun isUnReadSection(position: Int): Boolean {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                return false
            }

            var currentDay:Int?=null
            override fun isSection(position: Int): Boolean {

                //return position%3==0

                if (position < 0) return false


                if (position == 0) {

                    val current = TimeUtils.getDateFromMilli(adapter.getProgramItem(position)!!.event_timestamp);
                    val calendar = Calendar.getInstance()
                    calendar.time = current

                    currentDay = calendar.get(Calendar.YEAR) + calendar.get(Calendar.DAY_OF_YEAR)

                    return true
                } else {


                    val next = TimeUtils.getDateFromMilli(adapter.getProgramItem(position)!!.event_timestamp);

                   // val current = mAdapter.get().getMessageItem(position)
                   // val previous = mAdapter.get().getMessageItem(position + 1)

                    if (currentDay == null || next == null) return false

                    val calendar = Calendar.getInstance()
                    calendar.time = next
                    val previousDay = calendar.get(Calendar.YEAR) + calendar.get(Calendar.DAY_OF_YEAR)


                    val status:Boolean =currentDay != previousDay

                    currentDay = previousDay;

                    return status
                }

                /*return position == 0
                        || people.get(position)
                        .getLastName()
                        .charAt(0) != people.get(position - 1)
                        .getLastName()
                        .charAt(0);*/
            }



            override fun getSectionHeader(position: Int): CharSequence {

                //return "!"+position
                /*if (position < 0) {
                    return ""
                }*/
                val current:Date? = TimeUtils.getDateFromMilli(adapter.getProgramItem(position)!!.event_timestamp)

                //val current = mAdapter.get().getMessageItem(position)
                return if (current == null) "" else TimeUtils.formatDateChatHeader(current)
                //return  adapter.getProgramItem(position)!!.title + position
            }
        }
    }

}
