package com.workos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workos.R;
import com.workos.models.Task;

import java.util.ArrayList;
import java.util.List;


public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    List<Task> list;
    ItemClickListener itemClickListener;
    String projectName;

    public TasksRecyclerAdapter(ItemClickListener itemClickListener) {
        this.list = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    public void setTasksList(List<Task> tasks) {
        list = tasks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView projectTitle;
        private final TextView projectDescription;
        private final ImageView imageView;
        private final Context context;
        public final ImageView imgViewRemoveIcon;

        public ViewHolder(View v, Context context) {
            super(v);
            this.context = context;
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            projectTitle = v.findViewById(R.id.projectTitle);
            projectDescription = v.findViewById(R.id.projectDescription);
            imageView = v.findViewById(R.id.projectItemListImageView);
            imgViewRemoveIcon = v.findViewById(R.id.remove_icon);
        }

        public Context getContext() {
            return context;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row_item, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        Task model = list.get(position);
        // Define click listener for the ViewHolder's View.
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(model);
            }
        });
        holder.imgViewRemoveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = removeAt(position);
                TasksManager.removeTask(projectName, task);
            }
        });
        holder.projectTitle.setText(model.getName());
        holder.projectDescription.setText(model.getDescription());
    }

    public void addItem(Task task) {
        TasksManager.addTask(projectName, task, new TasksManager.MyCallback() {
            @Override
            public void onCallback(List<Task> tasks) {
                notifyDataSetChanged();
            }
        });
        //for the first time.
        if (!list.contains(task)) {
            list.add(task);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener {
        public void onItemClick(Task task);
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Task removeAt(int position) {
        Task removedTask = list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        return removedTask;
    }
}
