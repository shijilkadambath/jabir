package com.bigtime.mla.ui.login

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.bigtime.mla.R
import com.bigtime.mla.binding.FragmentDataBindingComponent
import com.bigtime.mla.common.autoCleared
import com.bigtime.mla.databinding.FragmentRegistrationStepOneBinding
import com.bigtime.mla.databinding.FragmentRegistrationStepTwoBinding
import com.bigtime.mla.di.Injectable
import com.bigtime.mla.ui.BaseFragment
import android.net.Uri.fromParts
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import com.bigtime.mla.data.model.Program
import android.widget.AdapterView
import com.bigtime.mla.data.api.Status


private const val TAG = "RegistrationStepOneFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationStepOneFragment : BaseFragment<FragmentRegistrationStepOneBinding>(), Injectable {

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var program: Program? = null
    private var progres: ProgressDialog? = null
    lateinit var mViewModel: LoginViewModel

    var binding by autoCleared<FragmentRegistrationStepOneBinding>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_registration_step_one,
                container,
                false,
                dataBindingComponent
        )

        binding.layoutBinder = this

        return binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
         super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(LoginViewModel::class.java)
        /*  binding.btnSubmit.setOnClickListener(View.OnClickListener {

              navController().navigate(
                      RegistrationStepOneFragmentDirections.showRegistrationStepTwo( binding.txtName.text.toString(),
                              binding.txtPassword.text.toString())
              )

          })*/

        progres = ProgressDialog(activity)

        program = RegistrationStepOneFragmentArgs.fromBundle(arguments).data


        binding.user = program

        // binding.spStatus.setOnItemClickListener()

        mViewModel.deleteRepo.observe(this, Observer {

            result->

            if (result?.status!= Status.LOADING) {

                if (result?.status == Status.SUCCESS && result.data != null && result.data.isSuccess()) {

                    onBackPressed()
                }

                progres!!.dismiss()
            }

        })
     }


    fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        try {
            val item: String = binding.spStatus.selectedItem.toString()
            //mBinding.status.text=item;
            program!!.status = item
            binding.user = program
            //binding.notifyChange()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun navController() = findNavController()

    fun onBackPressed() {
        activity!!.onBackPressed()
    }

    fun openSpinner() {
        binding.spStatus.performClick()
    }

    fun navigate() {

        val gmmIntentUri = Uri.parse("google.navigation:q=" + program!!.location)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun edit() {
        navController().navigate(
                RegistrationStepOneFragmentDirections.showAddEvent(program)
        )
    }

    fun delete() {

        val builder = AlertDialog.Builder(context!!)


        // Display a message on alert dialog
        builder.setMessage("Are you want to delete this event?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") { dialog, which ->
            dialog.dismiss()
            progres!!.show()
            mViewModel.deleteEvent(program!!.id)
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }

    fun share() {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, program!!.title
                    + "\nDate:" + program!!.dateString + "," + program!!.timeString
                    + "\nPlace:" + program!!.location
                    + "\nC/O:" + program!!.contact_name + "  " + program!!.contact_phone
                    + "\n\n" + program!!.description)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    fun callContactPerson() {

        val phone = program!!.contact_phone
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
    }
}
