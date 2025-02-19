package com.projeto.academicplanner.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.projeto.academicplanner.R;
import com.projeto.academicplanner.helper.ConfigFirebase;
import com.projeto.academicplanner.model.Student;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentUpdateFragment extends Fragment {

    private TextView backToPrevious, studentUniversity, studentCourse;
    private EditText studentFirstName, studentLastName, studentEmail;
    private ToggleButton isDelegateButton;
    private Button buttonStudents;
    private String idUserLogged;

    private StudentMainFragment studentMainFragmentF;
    private Student studentToUpdate;

    public StudentUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        studentToUpdate = (Student) getArguments().getSerializable("StudentToUpdate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View updateStudent = inflater.inflate(R.layout.fragment_student_update, container, false);


        //recovery loged user ID
        idUserLogged = ConfigFirebase.getUserId();

        initializingComponents(updateStudent);

        studentFirstName.setText(studentToUpdate.getStudentFirstName());
        studentLastName.setText(studentToUpdate.getStudentLastName());
        studentEmail.setText(studentToUpdate.getStudentEmail());
        isDelegateButton.setText(studentToUpdate.getStudentDelegate());
        studentUniversity.setText(studentToUpdate.getUniversityName());
        studentCourse.setText(studentToUpdate.getCourseName());

        /**
         * Setting action on buttons
         */

        backToPrevious.setOnClickListener(v -> {
            backToMain();
        });

        buttonStudents.setOnClickListener(v -> {
            studentUpdate();
        });

        return updateStudent;

    }

    private void studentUpdate() {

        String studentSaveDelegate = "NO";

        if (isDelegateButton.isChecked()) {
            studentSaveDelegate = "YES";
        }

        Student studentUpdate = new Student();

        studentUpdate.setIdUser(studentToUpdate.getIdUser());
        studentUpdate.setIdStudent(studentToUpdate.getIdStudent());
        studentUpdate.setStudentFirstName(studentFirstName.getText().toString());
        studentUpdate.setStudentLastName(studentLastName.getText().toString());
        studentUpdate.setStudentEmail(studentEmail.getText().toString());
        studentUpdate.setStudentDelegate(studentSaveDelegate);
        studentUpdate.setIdUniversity(studentToUpdate.getIdUniversity());
        studentUpdate.setUniversityName(studentToUpdate.getUniversityName());
        studentUpdate.setIdCourse(studentToUpdate.getIdCourse());
        studentUpdate.setCourseName(studentToUpdate.getCourseName());

        studentUpdate.update(studentUpdate);
        studentUpdate.updateStudentOnDiscipline(studentUpdate);

        toastMsg("Student " + studentUpdate.getStudentFirstName() + " successfully update");
        backToMain();

    }

    public void backToMain() {

        studentMainFragmentF = new StudentMainFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameSettingsMain, studentMainFragmentF);
        transaction.commit();
    }

    public void toastMsg(String text) {

        //show toast parameters
        Toast toastError = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        toastError.setGravity(Gravity.CENTER, 0, 800);
        toastError.show();
    }

    private void initializingComponents(View view) {

        backToPrevious = view.findViewById(R.id.backToPrevious);
        studentFirstName = view.findViewById(R.id.studentFirstName);
        studentLastName = view.findViewById(R.id.studentLastName);
        studentEmail = view.findViewById(R.id.studentEmail);
        isDelegateButton = view.findViewById(R.id.isDelegateButton);
        studentUniversity = view.findViewById(R.id.studentUniversity);
        studentCourse = view.findViewById(R.id.studentCourse);
        buttonStudents = view.findViewById(R.id.buttonStudents);

    }

}
