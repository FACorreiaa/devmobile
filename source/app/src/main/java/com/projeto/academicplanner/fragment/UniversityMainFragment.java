package com.projeto.academicplanner.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projeto.academicplanner.R;
import com.projeto.academicplanner.adapter.Adapter_Universities;
import com.projeto.academicplanner.helper.ConfigFirebase;
import com.projeto.academicplanner.model.Student;
import com.projeto.academicplanner.model.University;

import java.util.ArrayList;
import java.util.List;

public class UniversityMainFragment extends Fragment {

    //general variables
    private Button buttonUniversity;
    private TextView backToPrevious;
    private String idUserLogged;
    private List<University> universities = new ArrayList<>();
    private UniversityAddFragment addUniversityFragmentF;
    private UniversityUpdateFragment updateUniversityFragmentF;
    private SettingsFragment settingsFragment;

    //recycler view variables
    private RecyclerView recylcerUniversities;
    private RecyclerView.LayoutManager layout;
    private Adapter_Universities adapter;

    public UniversityMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mainUniversity = inflater.inflate(R.layout.fragment_university_main, container, false);

        //start configurations
        initializingComponents(mainUniversity);

        //recovery loged user ID
        idUserLogged = ConfigFirebase.getUserId();

        //call methods
        adapterConstructor();

        //create object and fill recyclerViewUniversities
        University university = new University();
        university.recovery(idUserLogged, universities, adapter);

        buttonUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNewFragment();
            }
        });

        backToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainSettings();
            }
        });


        return mainUniversity;

    }

    private void adapterConstructor() {

        //recycler view configuration
        layout = new LinearLayoutManager(getContext());
        adapter = new Adapter_Universities(universities, getContext());
        recylcerUniversities.setAdapter(adapter);
        recylcerUniversities.setLayoutManager(layout);
        recylcerUniversities.setHasFixedSize(true);
        //recylcerUniversities.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        adapter.setOnItemClickListener((adapter_universities, v, position) -> {

                final ImageView imageEdit = v.findViewById(R.id.imageEdit);
                final ImageView imageDelete = v.findViewById(R.id.imageDelete);

                final University objectToAction = universities.get(position);

                imageDelete.setOnClickListener(view -> {
                    universityDelete(objectToAction);
                });

                imageEdit.setOnClickListener(view -> {
                    goToUpdateFragment(objectToAction);
                });
        });

    }

    private void universityDelete(final University selectedToRemove) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String name = selectedToRemove.getUniversityName();
        String msg = "Are you sure, you want to delete the university " + name + "?";

        builder.setTitle(msg);
        builder.setPositiveButton(android.R.string.yes, (dialog, id) -> {

            selectedToRemove.delete();
            toastMsgLong("University " + name + " has been removed!");
            adapter.notifyDataSetChanged();
            dialog.dismiss();

            //call methods
            adapterConstructor();

            //create object and fill recyclerViewCourses
            University university = new University();
            university.recovery(idUserLogged, universities, adapter);

        });

        builder.setNegativeButton(android.R.string.no, (dialog, id) -> {
            //method to cancel the delete operation
            toastMsgLong("Request CANCELED");
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToNewFragment() {
        addUniversityFragmentF = new UniversityAddFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameSettingsMain, addUniversityFragmentF);
        transaction.commit();
    }

    public void goToUpdateFragment(University objectToAction) {
        updateUniversityFragmentF = new UniversityUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("universityIdBundle", objectToAction.getIdUniversity());
        bundle.putString("universityNameBundle", objectToAction.getUniversityName());
        bundle.putString("universityAcronymBundle", objectToAction.getUniversityAcronym());

        updateUniversityFragmentF.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameSettingsMain, updateUniversityFragmentF);
        transaction.commit();
    }

    /*public void goBackToMain() {

        fragmentMain = new AddEditMainFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameSettingsMain, fragmentMain);
        transaction.commit();

    }*/

    private void backToMainSettings() {

        settingsFragment = new SettingsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameSettingsMain, settingsFragment);
        transaction.commit();

    }

    public void toastMsgLong(String text) {

        //show toast parameters
        Toast toastError = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        toastError.setGravity(Gravity.CENTER, 0, 800);
        toastError.show();

    }

    private void initializingComponents(View view) {
        buttonUniversity = view.findViewById(R.id.buttonUniversity);
        backToPrevious = view.findViewById(R.id.backToPrevious);
        recylcerUniversities = view.findViewById(R.id.recyclerUniversities);
    }
}
