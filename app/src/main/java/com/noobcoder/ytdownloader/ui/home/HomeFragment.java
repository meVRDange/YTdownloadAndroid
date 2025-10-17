package com.noobcoder.ytdownloader.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.noobcoder.ytdownloader.api.ApiHelper;
import com.noobcoder.ytdownloader.databinding.FragmentHomeBinding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnHealthCheck.setOnClickListener(v -> {
            new Thread(() -> {
                String response = ApiHelper.makeRequest(requireContext(), "/helthCheck", "GET", null);

                requireActivity().runOnUiThread(() -> {
                    if (response != null) {
                        Toast.makeText(requireContext(), "✅ Health OK: " + response, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireContext(), "❌ Failed to contact server", Toast.LENGTH_LONG).show();
                    }
                });
            }).start();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}