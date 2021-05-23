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

import com.bumptech.glide.Glide;
import com.workos.R;
import com.workos.models.Project;

import java.util.ArrayList;
import java.util.List;


public class ProjectsRecyclerAdapter extends RecyclerView.Adapter<ProjectsRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    List<Project> list;
    ItemClickListener itemClickListener;


    public ProjectsRecyclerAdapter(ItemClickListener itemClickListener) {
        this.list = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    public void setProjectList(List<Project> projects) {
        list = projects;
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
            imgViewRemoveIcon = (ImageView) v.findViewById(R.id.remove_icon);
        }

        public Context getContext() {
            return context;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        Project model = list.get(position);
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
                Project project = removeAt(position);
                ProjectsManager.removeProject(project);
            }
        });
        if (model != null) {

            holder.projectTitle.setText(model.getProjectName());
            holder.projectDescription.setText(model.getProjectDescription());
            if (model.getProjectImagePath() != null) {

                Glide.with(holder.getContext()).load(model.getProjectImagePath()).into(holder.imageView);
                holder.imageView.setImageResource(R.drawable.profile);
            }
        }

    }

    public void addItem(Project project) {
        ProjectsManager.addProject(project, new ProjectsManager.MyCallback() {
            @Override
            public void onCallback(List<Project> projects) {
                notifyDataSetChanged();
            }
        });
        if (!list.contains(project)) {
            list.add(project);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener {
        public void onItemClick(Project project);
    }

    public Project removeAt(int position) {
        Project removedProject = list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        return removedProject;
    }
}
