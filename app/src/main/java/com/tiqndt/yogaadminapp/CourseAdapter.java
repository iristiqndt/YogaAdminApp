package com.tiqndt.yogaadminapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private Context context;

    public CourseAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item_layout, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textViewCourseType.setText(course.getType());
        holder.textViewCourseDayTime.setText(course.getDay() + " - " + course.getTime());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCourseType, textViewCourseDayTime;

        public CourseViewHolder(View view) {
            super(view);
            textViewCourseType = view.findViewById(R.id.textViewCourseType);
            textViewCourseDayTime = view.findViewById(R.id.textViewCourseDayTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Course clickedCourse = courseList.get(position);
                        Intent intent = new Intent(context, CourseDetailActivity.class);
                        intent.putExtra("COURSE_ID", clickedCourse.getId());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}