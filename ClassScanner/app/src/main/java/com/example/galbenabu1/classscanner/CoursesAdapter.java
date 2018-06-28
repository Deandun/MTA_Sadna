package com.example.galbenabu1.classscanner;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesViewHolder> {
    private final String TAG = "CoursessAdapter";

    private List<Course> mCoursesList;

    public CoursesAdapter(List<Course> coursesList) {
        mCoursesList = coursesList;
    }

    @Override
    public CoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e(TAG,"onCreateViewHolder() >>");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);

        Log.e(TAG,"onCreateViewHolder() <<");
        return new CoursesViewHolder(parent.getContext(),itemView);
    }

    @Override
    public void onBindViewHolder(CoursesViewHolder holder, int position) {

        Log.e(TAG,"onBindViewHolder() >> " + position);

        Course course = mCoursesList.get(position);

        // bind Course data to it's view items
        holder.setSelectedCourse(course);
        holder.getCourseName().setText("Name: " + course.getCourseName());
        holder.getCreatorName().setText("Publisher: " + course.getPublisherName());
        holder.getCreationDate().setText("Creation date: " + course.getmCreationDate().toString());


        Log.e(TAG,"onBindViewHolder() << "+ position);
    }

    @Override
    public int getItemCount() {
        return mCoursesList.size();
    }
}