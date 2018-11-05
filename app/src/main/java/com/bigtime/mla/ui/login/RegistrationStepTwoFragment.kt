package com.bigtime.mla.ui.login

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment

import com.wafflecopter.multicontactpicker.ContactResult
import javax.inject.Inject
import android.Manifest

import android.support.v4.content.ContextCompat
import android.widget.AbsListView.CHOICE_MODE_MULTIPLE
import com.wafflecopter.multicontactpicker.LimitColumn
import com.wafflecopter.multicontactpicker.MultiContactPicker
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.bigtime.mla.AppExecutors

import com.bigtime.mla.R
import com.bigtime.mla.common.autoCleared
import com.bigtime.mla.data.api.Status
import com.bigtime.mla.data.model.Program
import com.bigtime.mla.databinding.FragmentRegistrationStepTwoBinding
import com.bigtime.mla.di.Injectable
import com.bigtime.mla.ui.BaseFragment
import com.bigtime.mla.ui.picker.DatePickerFragment
import com.bigtime.mla.ui.picker.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "RegistrationStepTwoFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationStepTwoFragment : BaseFragment<FragmentRegistrationStepTwoBinding>(), Injectable {

    override fun getLayoutId(): Int {
        return   R.layout.fragment_registration_step_two//To change body of created functions use File | Settings | File Templates.
    }
    private val permissionsRequired = arrayOf(Manifest.permission.READ_CONTACTS)
    private val PERMISSION_CALLBACK_CONSTANT = 0x1
    private val CONTACT_PICKER_REQUEST:Int = 0x2
    private val REQUEST_PERMISSION_SETTING = 0x3


    private var program: Program? = null
    private var sentToSettings = false
    @Inject
    lateinit var appExecutors: AppExecutors

    private var progres: ProgressDialog? = null
    private var timeCalender: Calendar? = Calendar.getInstance()

    //var adapter by autoCleared<ContactListAdapter>()
    var adapter by autoCleared<ContactListAdapter>()


    var dateFragment: DatePickerFragment? = null
    var timeFragment: TimePickerFragment? = null

    lateinit var mViewModel: LoginViewModel

    /*override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_registration_step_two, container,
                false, dataBindingComponent)
        mBinding.layoutBinder = this
        return mBinding.root
    }*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(LoginViewModel::class.java)
        program = RegistrationStepTwoFragmentArgs .fromBundle(arguments).data
        mBinding.user = program
        mBinding.layoutBinder = this

        dateFragment =DatePickerFragment()


        dateFragment!!.setListener(object :  DatePickerFragment.OnDateSetListener{

            override fun onDateSet(y: Int, m: Int, d: Int){

                timeCalender!!.set(Calendar.YEAR,y)
                timeCalender!!.set(Calendar.DAY_OF_MONTH,d)
                timeCalender!!.set(Calendar.MONTH,m)


                val formatDate = SimpleDateFormat("MMMM dd yyyy")
                val formattedTime = formatDate.format(timeCalender!!.time).toString()

                //var x:String = String.format("%02d", d)+ "-" +String.format("%02d", m) + "-"+y;
                mBinding.edtDate.setText(formattedTime)

            }

        })
        progres = ProgressDialog(activity)


        timeFragment = TimePickerFragment()


        timeFragment!!.setListener(object :  TimePickerFragment.OnTimeSetListener{

            override fun onDateSet(h: Int, m: Int){

                timeCalender!!.set(Calendar.HOUR_OF_DAY,h)
                timeCalender!!.set(Calendar.MINUTE,m)


                val formatDate = SimpleDateFormat("hh:mm aa")
                val formattedTime = formatDate.format(timeCalender!!.time).toString()

                //var x:String = String.format("%02d", h)+":"+String.format("%02d", m)
                mBinding.edtFromTime.setText(formattedTime)
            }

        })

        adapter = ContactListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors) {

        }

        adapter.submitList(ArrayList<ContactResult>())

        mBinding.listUser.adapter = adapter

        mViewModel.postRepo.observe(this, Observer {

            result->

            if (result?.status!= Status.LOADING) {

                if (result?.status == Status.SUCCESS && result?.data != null && result?.data!!.isSuccess()) {

                    Toast.makeText(activity,result?.data!!.message,Toast.LENGTH_LONG).show()

                    onBackPressed()
                }

                progres!!.dismiss()
            }

        })

        mBinding.spType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                mBinding.edtType.setText(mBinding.spType.selectedItem.toString())

            }

        }

        mBinding.spStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                mBinding.edtStatus.setText(mBinding.spStatus.selectedItem.toString())

            }

        }

        if (!(program!!.id.equals(Integer(0)))){

            val formatTime = SimpleDateFormat("HH:mm:ss")
            val time:Date = formatTime.parse(program!!.time)
            val calander:Calendar= Calendar.getInstance()
            calander.time=time

            val formatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date:Date = formatDate.parse(program!!.date)
            timeCalender!!.time=date

            timeCalender!!.set(Calendar.HOUR_OF_DAY,calander.get(Calendar.HOUR_OF_DAY))
            timeCalender!!.set(Calendar.MINUTE,calander.get(Calendar.MINUTE))



            val formatDate1 = SimpleDateFormat("hh:mm aa")
            val formattedTime = formatDate1.format(timeCalender!!.time).toString()
            mBinding.edtFromTime.setText(formattedTime)

            val formatTime1 = SimpleDateFormat("MMMM dd yyyy")
            val formattedTime1 = formatTime1.format(timeCalender!!.time).toString()
            mBinding.edtDate.setText(formattedTime1)

        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted

            if (grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted()// able to get permission
                } else {
                    if (!shouldShowRequestPermissionRationale(permissionsRequired[0])) {

                        val message = getString(R.string.msg_permission_settings, getString(R.string.app_name), "Contact")
                        showPermissionSettingsDialog(REQUEST_PERMISSION_SETTING, message,null)
                        sentToSettings = true
                    } else {
                        permissionGranted()// able to get permission
                    }
                }
            }
        }
    }

    protected fun showPermissionSettingsDialog(requstCode: Int, message: String, cancelListener: DialogInterface.OnClickListener?) {

        AlertDialog.Builder(activity!!)
                //.setTitle("Delete entry")
                .setCancelable(true)
                .setMessage(message)
                .setPositiveButton(R.string.settings) { dialog, which -> gotoPermissionSettings(requstCode) }
                .setNegativeButton(android.R.string.no, cancelListener)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    private fun gotoPermissionSettings(requstCode: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity!!.packageName, null)
        intent.data = uri
        startActivityForResult(intent, requstCode)
    }


    fun onBackPressed(){
        activity!!.onBackPressed()
    }

    fun pickTime(){
        timeFragment!!.show(childFragmentManager, "datePicker")
    }

    fun pickDate(){
        dateFragment!!.show(childFragmentManager, "timePicker")
    }

    fun pickContact(){

        /*mBinding.get().setAuthCompleted(true)
        mBinding.get().txtTitle.setText(R.string.title_initialize_account)
        mBinding.get().txtSubTitle.setText(getString(R.string.msg_sync_account, getString(R.string.app_name)))
        mBinding.get().imgLogo.setImageResource(R.drawable.verifying_user)

        mBinding.get().txtStatus.setText(getString(R.string.msg_permission_contact_storage, getString(R.string.app_name)))*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(activity!!, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionsRequired[0]) ) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(activity!!)
                builder.setMessage(getString(R.string.msg_permission_contact_storage, getString(R.string.app_name)))
                builder.setCancelable(false)
                builder.setPositiveButton(R.string.grand) { dialog, which ->
                    dialog.cancel()
                    requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton(R.string.cancel) { dialog, which ->
                    dialog.cancel()
                    //mBinding.get().setIsLoading(false)
                }
                builder.show()
            } else {
                //just request the permission
                requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }


        } else {
            //You already have the permission, just go ahead.
            permissionGranted()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val results:ArrayList<ContactResult> = MultiContactPicker.obtainResult(data!!)
                //Log.d("MyTag", results[0].displayName)
                adapter.submitList(results)
            } else if (resultCode == RESULT_CANCELED) {
                println("User closed the picker without selecting items.")
            }
        }else if (requestCode == REQUEST_PERMISSION_SETTING){
            permissionGranted()
        }
    }

    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            permissionGranted()
        }
    }

    private fun permissionGranted() {
        if (ActivityCompat.checkSelfPermission(activity!!, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
            //Got contact Permission
            MultiContactPicker.Builder(this@RegistrationStepTwoFragment) //Activity/fragment context
                    //.theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                    .hideScrollbar(false) //Optional - default: false
                    .showTrack(true) //Optional - default: true
                    .searchIconColor(Color.WHITE) //Option - default: White
                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                    .handleColor(ContextCompat.getColor(this!!.activity!!, R.color.primary)) //Optional - default: Azure Blue
                    .bubbleColor(ContextCompat.getColor(this!!.activity!!, R.color.primary)) //Optional - default: Azure Blue
                    .bubbleTextColor(Color.WHITE) //Optional - default: White
                    .setTitleText("Select Contacts") //Optional - default: Select Contacts
                    //.setSelectedContacts()
                    //.setSelectedContacts("10", "5" / myList) //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                    .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                    .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                    .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in,
                            android.R.anim.fade_out) //Optional - default: No animation overrides
                    .showPickerForResult(CONTACT_PICKER_REQUEST)
        }

    }

    fun save() {
        mBinding.tlName.setErrorEnabled(false)

        mBinding.edtName.clearFocus()

        var title:String = mBinding.edtName.text.toString();
        var des:String = mBinding.edtDes.text.toString();
        var co_name:String = mBinding.edtCoName.text.toString();
        var co_number:String = mBinding.edtCoNumber.text.toString();
        var loc:String = mBinding.edtLocation.text.toString();
        var type:String = mBinding.edtType.text.toString();
        var date:String = mBinding.edtDate.text.toString();
        var time:String = mBinding.edtFromTime.text.toString();
        var status:String = mBinding.edtStatus.text.toString();

        if (title.isEmpty()){
            mBinding.edtName.error="please enter"
            //mBinding.tlName.isErrorEnabled=true
            //mBinding.tlName.error="please enter"
        }else if (des.isEmpty()){
            //mBinding.tlDes.error="please enter"
            mBinding.edtDes.error="please enter"
        }else if (co_name.isEmpty()){
            //mBinding.tlCoName.error="please enter"
            mBinding.edtCoName.error="please enter"
        }else if (co_number.isEmpty()){
            //mBinding.tlCoNumber.error="please enter"
            mBinding.edtCoNumber.error="please enter"
        }else if (loc.isEmpty()){
            mBinding.edtLocation.error="please enter"
            //mBinding.tlLocation.error="please enter"
        }else if (type.isEmpty()){
            mBinding.edtType.error="please enter"
            mBinding.tlType.error="please enter"
        }else if (date.isEmpty()){
            mBinding.edtDate.error="please enter"
            //mBinding.tlDate.error="please enter"
        }else if (time.isEmpty()){
            mBinding.edtFromTime.error="please enter"
            //mBinding.tlFromTime.error="please enter"
        }else if (status.isEmpty()){
            mBinding.edtStatus.error="please enter"
            //mBinding.tlStatus.error="please enter"
        }else{
            progres!!.show()

            program!!.title=title
            program!!.description=des
            program!!.contact_phone=co_number
            program!!.contact_name=co_name
            program!!.location=loc
            program!!.type=type
            program!!.status=status
            program!!.timeString=time
            program!!.dateString=date

            //2018-10-20T09:00:04.000Z
            val formatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val formattedDate = formatDate.format(timeCalender!!.time).toString()
            program!!.date=formattedDate

            val formatTime = SimpleDateFormat("HH:mm:ss")
            val formattedTime = formatTime.format(timeCalender!!.time).toString()
            program!!.time=formattedTime

            mViewModel.postEvent(program!!)
        }


    }
}
