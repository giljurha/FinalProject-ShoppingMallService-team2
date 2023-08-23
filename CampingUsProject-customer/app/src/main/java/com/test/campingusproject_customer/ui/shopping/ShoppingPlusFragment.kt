package com.test.campingusproject_customer.ui.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentShoppingPlusBinding

class ShoppingPlusFragment : Fragment() {
    lateinit var fragmentShoppingPlusBinding: FragmentShoppingPlusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShoppingPlusBinding = FragmentShoppingPlusBinding.inflate(layoutInflater)

        return inflater.inflate(R.layout.fragment_shopping_plus, container, false)
    }
}