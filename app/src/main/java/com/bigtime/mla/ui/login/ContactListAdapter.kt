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

package com.bigtime.mla.ui.login

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bigtime.mla.AppExecutors
import com.bigtime.mla.R
import com.bigtime.mla.ui.BaseDataBindListAdapter
import com.wafflecopter.multicontactpicker.ContactResult

import com.bigtime.mla.databinding.ItemContactBinding
import android.databinding.adapters.TextViewBindingAdapter.setText



/**
 * A RecyclerView adapter for [Repo] class.
 */
class ContactListAdapter(

        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val itemClickCallback: ((ContactResult) -> Unit)?
) : BaseDataBindListAdapter<ContactResult, ItemContactBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<ContactResult>() {
            override fun areItemsTheSame(oldItem: ContactResult, newItem: ContactResult): Boolean {
                return oldItem.contactID.equals( newItem.contactID)
            }

            override fun areContentsTheSame(oldItem: ContactResult, newItem: ContactResult): Boolean {
                return oldItem.contactID == newItem.contactID
                        && oldItem.displayName == newItem.displayName
            }
        }
) {

    override fun createBinding(parent: ViewGroup): ItemContactBinding {
        val binding = DataBindingUtil.inflate<ItemContactBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_contact,
                parent,
                false,
                dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.user?.let {

                itemClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemContactBinding, item: ContactResult) {
        binding.user = item
        //item.phoneNumbers.get(0).number
        //binding.desc.setText(TextUtils.join(", ", item.phoneNumbers) )

        val sb = StringBuilder()
        var count = 1
        for (u in item.phoneNumbers) {
            sb.append(u.number+", ")
        }
        binding.txtNumber.setText(sb.toString())
    }
}
