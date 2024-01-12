package com.jh237.Greenspot.MenuItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.jh237.Greenspot.databinding.FragmentWebviewHelpBinding

class HelpWebViewFragment : Fragment() {

    private var _binding: FragmentWebviewHelpBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebviewHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myWebView: WebView = binding.webview
        myWebView.loadUrl("https://identify.plantnet.org")

    }

}