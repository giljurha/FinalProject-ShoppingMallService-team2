package com.test.campingusproject_customer.ui.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentShoppingProductBinding

class ShoppingProductFragment : Fragment() {
    lateinit var fragmentShoppingProductBinding: FragmentShoppingProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShoppingProductBinding = FragmentShoppingProductBinding.inflate(layoutInflater)

        return fragmentShoppingProductBinding.root
    }
}