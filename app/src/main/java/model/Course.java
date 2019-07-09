package model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import adapter.Adapter_Courses;
import helper.ConfigFirebase;

public class Course extends University{

    private String idUser;
    private String idCourse;
    private String courseName;
    private String acronymCourse;
    private DatabaseReference firebaseRef = ConfigFirebase.getReferenciaFirebase();

    public Course() {

        DatabaseReference courseRef = firebaseRef
                .child("courses");
        setIdCourse(courseRef.push().getKey());

    }

    public void saveCourseData() {

        DatabaseReference courseRef = firebaseRef
                .child("courses")
                .child(getIdUser())
                .child(getIdCourse());
        courseRef.setValue(this);

    }

    public void updateCourseData(Course objectToUpdate) {

        DatabaseReference courseRef = firebaseRef
                .child("courses")
                .child(getIdUser())
                .child(getIdCourse());
        courseRef.setValue(objectToUpdate);

    }

    public void deleteCourseData() {

        DatabaseReference courseRef = firebaseRef
                .child("courses")
                .child(getIdUser())
                .child(getIdCourse());
        courseRef.removeValue();
    }

    public void recoveryCourses(String idUserLoged, final List<Course> courses, final Adapter_Courses adapter) {

        DatabaseReference courseRef = firebaseRef
                .child("courses")
                .child(idUserLoged);

        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                courses.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    courses.add(ds.getValue(Course.class));

                }

                //put the item added to the top
                Collections.reverse(courses);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }


    public String getIdUser() { return idUser; }

    public void setIdUser(String idUser) { this.idUser = idUser; }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAcronymCourse() {
        return acronymCourse;
    }

    public void setAcronymCourse(String acronymCourse) {
        this.acronymCourse = acronymCourse;
    }

}
