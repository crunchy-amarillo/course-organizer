package com.iu.course_organizer.ui.learning_unit.notes;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.course_organizer.R;
import com.iu.course_organizer.database.model.LearningUnitNote;

import java.util.List;

public class LearningUnitNoteListEntryAdapter
        extends RecyclerView.Adapter<LearningUnitNoteListEntryAdapter.ViewHolder> {

    private final List<LearningUnitNote> learningUnitNotes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView note;

        public ViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.learningUnitNote);
        }

        public TextView getNote() {
            return note;
        }
    }

    public LearningUnitNoteListEntryAdapter(List<LearningUnitNote> learningUnitNotes) {
        this.learningUnitNotes = learningUnitNotes;
    }

    public void setLearningUnitNotes(List<LearningUnitNote> learningUnitNotes) {
        this.learningUnitNotes.clear();
        this.learningUnitNotes.addAll(learningUnitNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.learning_unit_note_list_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getNote().setText(learningUnitNotes.get(position).note);
    }

    public LearningUnitNote getByPosition(int position) {
        return learningUnitNotes.get(position);
    }

    @Override
    public int getItemCount() {
        return null == this.learningUnitNotes ? 0 : this.learningUnitNotes.size();
    }
}
