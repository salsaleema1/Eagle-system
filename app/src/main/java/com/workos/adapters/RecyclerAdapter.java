package com.workos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.workos.R;
import com.workos.models.Project;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    List<Project> list;
    ItemClickListener itemClickListener;

    public RecyclerAdapter(List<Project> list, ItemClickListener itemClickListener) {
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView projectTitle;
        private final TextView projectDescription;
        private final CircleImageView imageView;
        private final Context context;

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
        holder.projectTitle.setText(model.getProjectName());
        holder.projectDescription.setText(model.getProjectDescription());
        if (model.getProjectImagePath() != null) {

            Glide.with(holder.getContext()).load(model.getProjectImagePath()).into(holder.imageView);
            holder.imageView.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener {
        public void onItemClick(Project project);
    }
}
