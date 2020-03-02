package com.example.bakingapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsListViewHolder> {

    private List<Step> mSteps;
    private OnStepClickListener mStepClickListener;

    public interface OnStepClickListener {
        void OnStepClicked(Step step);
    }

    public StepsListAdapter(List<Step> steps, OnStepClickListener onStepClickListener) {
        mSteps = steps;
        mStepClickListener = onStepClickListener;
    }

    @NonNull
    @Override
    public StepsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdItem = R.layout.item_step;
        View view = inflater.inflate(layoutIdItem, parent, false);
        return new StepsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsListViewHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.bindStep(step);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class StepsListViewHolder extends RecyclerView.ViewHolder {
        private Step mStep;

        @BindView(R.id.tv_step_description)
        TextView mStepDescription;

        @OnClick(R.id.cv_step_item)
        void stepClickListener() {
            mStepClickListener.OnStepClicked(mStep);
        }

        StepsListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindStep(Step step) {
            String stepDescription = step.getDescription();
            mStepDescription.setText(stepDescription);
            mStep = step;
        }
    }
}
