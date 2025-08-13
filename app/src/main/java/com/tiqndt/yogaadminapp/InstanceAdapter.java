package com.tiqndt.yogaadminapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InstanceAdapter extends RecyclerView.Adapter<InstanceAdapter.InstanceViewHolder> {

    private static final String TAG = "YOGA_APP_DEBUG";

    private List<ClassInstance> instanceList;
    private Context context;
    private String courseDayOfWeek;

    public InstanceAdapter(List<ClassInstance> instanceList, Context context, String courseDayOfWeek) {
        this.instanceList = instanceList;
        this.context = context;
        this.courseDayOfWeek = courseDayOfWeek;
    }

    @NonNull
    @Override
    public InstanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instance_item_layout, parent, false);
        return new InstanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InstanceViewHolder holder, int position) {
        ClassInstance instance = instanceList.get(position);
        holder.textViewInstanceDate.setText("Date: " + instance.getDate());
        holder.textViewTeacherName.setText("Teacher: " + instance.getTeacherName());

        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "InstanceAdapter - Clicked on instance with ID: " + instance.getId());
            Log.d(TAG, "InstanceAdapter - Sending courseId: " + instance.getCourseId());
            Log.d(TAG, "InstanceAdapter - Sending courseDayOfWeek: " + this.courseDayOfWeek);

            Intent intent = new Intent(context, AddEditInstanceActivity.class);
            intent.putExtra("INSTANCE_ID", instance.getId());
            intent.putExtra("COURSE_ID", instance.getCourseId());
            intent.putExtra("COURSE_DAY_OF_WEEK", this.courseDayOfWeek);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return instanceList.size();
    }

    public void updateInstances(List<ClassInstance> newInstances) {
        this.instanceList.clear();
        this.instanceList.addAll(newInstances);
        notifyDataSetChanged();
    }

    public static class InstanceViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewInstanceDate, textViewTeacherName;

        public InstanceViewHolder(View view) {
            super(view);
            textViewInstanceDate = view.findViewById(R.id.textViewInstanceDate);
            textViewTeacherName = view.findViewById(R.id.textViewTeacherName);
        }
    }
}