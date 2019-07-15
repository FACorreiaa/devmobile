package com.projeto.academicplanner.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.academicplanner.R;
import com.projeto.academicplanner.activity.NavMainActivity;
import com.projeto.academicplanner.helper.ConfigFirebase;
import com.projeto.academicplanner.model.UserProfile;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private TextView txtName, txtEmail, backToHome;
    private ImageView imgProfile;
    private Button btnEditUserProfile;

    private FirebaseAuth auth;
    private DatabaseReference firebaseRef;

    private String userIdLogged;
    private String urlImagemSelecionada;

    private UserProfileEditFragment userProfileEditFragment;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View v =  inflater.inflate(R.layout.fragment_user_profile, container, false);


        /**
         * Initializing Firebase
         */
        auth = ConfigFirebase.getReferenciaAutenticacao();
        firebaseRef = ConfigFirebase.getReferenciaFirebase();
        userIdLogged = ConfigFirebase.getUserId();


        /**
         * Initializing components from activity
         */
        initializingComponents(v);


        /**
         * Recovering user profile data
         */
        userDataRecovery();

        /**
         * Go to edit user profile
         */
        btnEditUserProfile.setOnClickListener( view ->  {

            showUserProfileEditFragment();

        });

        /**
         * Back to home
         */
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NavMainActivity.class));
            }
        });


        return v;

    }


    public void userDataRecovery(){

        DatabaseReference userProfileRef = firebaseRef
                .child("users")
                .child( userIdLogged );
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null){

                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                    String firstName = userProfile.getFirstname();
                    String lastname = userProfile.getLastname();

                    String name;

                    if( firstName.isEmpty() || lastname.isEmpty() ){
                        name = "Sem nome";
                    } else{
                        name = firstName + " " + lastname;
                    }

                    txtName.setText(name);
                    txtEmail.setText(auth.getCurrentUser().getEmail());

                    urlImagemSelecionada = userProfile.getUrlProfile();

                    if(urlImagemSelecionada != ""){
                        Picasso.get()
                                .load(urlImagemSelecionada)
                                .into(imgProfile);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUserProfileEditFragment(){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        userProfileEditFragment = new UserProfileEditFragment();
        transaction.replace(R.id.frameAddEditUserProfile, userProfileEditFragment);
        transaction.commit();
    }

    private void initializingComponents(View v){

        imgProfile = v.findViewById(R.id.imgProfile);
        txtEmail = v.findViewById(R.id.txtEmail);
        txtName = v.findViewById(R.id.txtName);
        btnEditUserProfile = v.findViewById(R.id.btnGoToNextFragment);
        backToHome = v.findViewById(R.id.backToHome);

    }

}
