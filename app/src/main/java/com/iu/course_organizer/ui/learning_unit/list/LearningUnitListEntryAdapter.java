package com.iu.course_organizer.ui.learning_unit.list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.course_organizer.R;
import com.iu.course_organizer.database.model.LearningUnit;

import java.util.List;

public class LearningUnitListEntryAdapter
        extends RecyclerView.Adapter<LearningUnitListEntryAdapter.ViewHolder> {

    private final List<LearningUnit> learningUnits;

    View.OnClickListener clickListener;
    OnStartButtonClickListener startButtonClickListener;
    OnStopButtonClickListener stopButtonClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView workingHours;
        private final TextView timer;
        private final ProgressBar progressBar;
        private final Button btnStart;
        private final Button btnStop;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.learningUnitListTitle);
            description = view.findViewById(R.id.learningUnitListDescription);
            workingHours = view.findViewById(R.id.learningUnitListWorkingHours);
            timer = view.findViewById(R.id.learningUnitTimer);
            progressBar = view.findViewById(R.id.progressBar);

            btnStart = view.findViewById(R.id.btnStartTimeTracking);
            btnStop = view.findViewById(R.id.btnStopTimeTracking);
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getWorkingHours() {
            return workingHours;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public TextView getTimer() {
            return timer;
        }
    }

    public LearningUnitListEntryAdapter(List<LearningUnit> courses) {
        this.learningUnits = courses;
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setStartButtonListener(OnStartButtonClickListener buttonListener) {
        this.startButtonClickListener = buttonListener;
    }

    public void setStopButtonListener(OnStopButtonClickListener buttonListener) {
        this.stopButtonClickListener = buttonListener;
    }

    public void setLearningUnits(List<LearningUnit> learningUnits) {
        this.learningUnits.clear();
        this.learningUnits.addAll(learningUnits);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.learning_unit_list_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (null != clickListener) {
            viewHolder.itemView.setOnClickListener(clickListener);
        }

        if (null != startButtonClickListener) {
            viewHolder.btnStart.setOnClickListener(
                    view -> startButtonClickListener.onClick(view, position));
        }

        if (null != stopButtonClickListener) {
            viewHolder.btnStop.setOnClickListener(
                    view -> stopButtonClickListener.onClick(view, position));
        }

        viewHolder.getDescription().setText(learningUnits.get(position).description);
        viewHolder.getTitle().setText(learningUnits.get(position).title);

        Integer spentMinutes = learningUnits.get(position).spentMinutes;
        Integer workingHours = learningUnits.get(position).workingHours;

        int progressPercentage = 0;
        int hours = 0;
        int minutes = 0;
        if (null != spentMinutes && 0 < spentMinutes) {
            minutes = spentMinutes % 60;
            hours = (int) Math.floor((float) spentMinutes / 60);
            progressPercentage = Math.round(spentMinutes * 100 / ((float) workingHours * 60));
        }
        String spentTime = hours + "h " + minutes + "m";

        viewHolder.getWorkingHours().setText(spentTime + " / " + workingHours.toString() + "h");

        viewHolder.progressBar.setProgress(progressPercentage);
    }

    public LearningUnit getByPosition(int position) {
        return learningUnits.get(position);
    }

    @Override
    public int getItemCount() {
        return null == this.learningUnits ? 0 : this.learningUnits.size();
    }
}
