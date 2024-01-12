package com.jh237.Greenspot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*
We import AppCompatActivity to host our application.
AppCompatActivity is the base class for activities, while supporting for older Android versions by adding backported features.
https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity

Bundle allows to pass data between activities. This is useful during state changes and navigation.
https://developer.android.com/reference/android/os/Bundle
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* The displayed fragment is set here. This fragment contains a FragmentContainerView that contains the NavHostFragment.
        https://developer.android.com/reference/androidx/fragment/app/FragmentContainerView

        The NavHostFragment is responsible for managing the navigation between fragments.
        https://developer.android.com/reference/androidx/navigation/fragment/NavHostFragment

        The first fragment in the NavHostFragment is set here. Being ListFragment.

         */
    }
}