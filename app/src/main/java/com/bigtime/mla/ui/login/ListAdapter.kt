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
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bigtime.mla.AppExecutors
import com.bigtime.mla.R
import com.bigtime.mla.data.model.Program
import com.bigtime.mla.databinding.ItemProgramsBinding
import com.bigtime.mla.ui.BaseDataBindListAdapter

/**
 * A RecyclerView adapter for [Repo] class.
 */
class ListAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val itemClickCallback: ((Program) -> Unit)?
) : BaseDataBindListAdapter<Program, ItemProgramsBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Program>() {
            override fun areItemsTheSame(oldItem: Program, newItem: Program): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Program, newItem: Program): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.title == newItem.title
            }
        }
) {

    override fun createBinding(parent: ViewGroup): ItemProgramsBinding {
        val binding = DataBindingUtil.inflate<ItemProgramsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_programs,
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

    override fun bind(binding: ItemProgramsBinding, item: Program) {
        binding.user = item
    }

    fun getProgramItem(pos: Int): Program? {
        return super.getItem(pos)
    }
}
