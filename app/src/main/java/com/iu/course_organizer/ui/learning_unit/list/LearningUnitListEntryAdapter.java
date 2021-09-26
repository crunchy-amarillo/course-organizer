package com.iu.course_organizer.ui.learning_unit.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.course_organizer.R;
import com.iu.course_organizer.database.model.LearningUnit;

import java.util.List;

public class LearningUnitListEntryAdapter
        extends RecyclerView.Adapter<LearningUnitListEntryAdapter.ViewHolder> {

    private final List<LearningUnit> learningUnits;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView workingHours;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.learningUnitListTitle);
            description = view.findViewById(R.id.learningUnitListDescription);
            workingHours = view.findViewById(R.id.learningUnitListWorkingHours);
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
    }

    public LearningUnitListEntryAdapter(List<LearningUnit> courses) {
        this.learningUnits = courses;
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

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getDescription().setText(learningUnits.get(position).description);
        viewHolder.getTitle().setText(learningUnits.get(position).title);
        viewHolder.getWorkingHours().setText("0 / " + learningUnits.get(position).workingHours.toString());
    }

    public LearningUnit getByPosition(int position) {
        return learningUnits.get(position);
    }

    @Override
    public int getItemCount() {
        return null == this.learningUnits ? 0 : this.learningUnits.size();
    }
}
