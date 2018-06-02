package com.example.galbenabu1.classscanner;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CoursesViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = "CourseViewHolder";
    private CardView mCourseCardView;
    private TextView mCreatorName;
    private TextView mCourseName;
    private TextView mCreationDate;
    private ImageView mCourseImg;
    private Course mSelectedCourse;

    public CoursesViewHolder(Context context, View itemView) {
        super(itemView);

        mCourseCardView = itemView.findViewById(R.id.cvCourse);
        mCreatorName = itemView.findViewById(R.id.tvPublisher);
        mCourseName = itemView.findViewById(R.id.tvCourseName);
        mCreationDate = itemView.findViewById(R.id.tvCreationDate);
        mCourseImg = itemView.findViewById(R.id.ivCourseImage);

        mCourseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.e(TAG, "CardView.onClick() >> Dare =" + mSelectedDare.toString());

                Context context = view.getContext();
                //Intent intent = new Intent(context, DareDetailsActivity.class);
                //intent.putExtra("course", mSelectedCourse);
                //context.startActivity(intent);
            }
        });
    }

    public Course getSelectedCourse() {
        return mSelectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.mSelectedCourse = selectedCourse;
    }

    public TextView getCreatorName() {
        return mCreatorName;
    }

    public void setCreatorName(TextView creatorName) {
        this.mCreatorName = creatorName;
    }

    public TextView getCourseName() {
        return mCourseName;
    }

    public void setCourseName(TextView courseName) {
        this.mCourseName = courseName;
    }

    public TextView getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(TextView creationDate) {
        this.mCreationDate = creationDate;
    }

    public ImageView getCourseImg() {
        return mCourseImg;
    }

    public void setCourseImg(ImageView courseImg) {
        this.mCourseImg = courseImg;
    }
}
