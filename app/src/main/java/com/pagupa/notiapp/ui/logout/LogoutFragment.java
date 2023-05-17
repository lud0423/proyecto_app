package com.pagupa.notiapp.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pagupa.notiapp.clases.Server;
import com.pagupa.notiapp.databinding.FragmentLogoutBinding;
import com.pagupa.notiapp.ui.login.LoginActivity;

public class LogoutFragment extends Fragment {
    private FragmentLogoutBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogoutViewModel homeViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);

        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Server.borrarPreferencias(getActivity());
        Intent main = new Intent(getActivity(), LoginActivity.class);

        main.putExtra("login", true);
        startActivity(main);
        //getActivity().finish();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}