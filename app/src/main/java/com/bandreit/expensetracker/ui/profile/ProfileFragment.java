package com.bandreit.expensetracker.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.ui.signIn.SignInActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        FloatingActionButton floatingActionButtonSettings = root.findViewById(R.id.settings_FAB);
        FloatingActionButton floatingActionButtonLogOut = root.findViewById(R.id.signOut_FAV);

        TextView name = root.findViewById(R.id.name);
        TextView provider = root.findViewById(R.id.provider);
        TextView email = root.findViewById(R.id.email);
        CircleImageView profileImage = root.findViewById(R.id.profile_image);

        floatingActionButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_settingsFragment);
            }
        });

        floatingActionButtonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.signOut();
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        });

        mViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                UserInfo profile = firebaseUser.getProviderData().get(0);
                name.setText(profile.getDisplayName());
                email.setText(profile.getEmail());
                provider.setText(profile.getProviderId());
                Uri photoUrl = profile.getPhotoUrl();
                Picasso.get()
                        .load(photoUrl)
                        .into(profileImage);
            }
        });
        return root;
    }

}