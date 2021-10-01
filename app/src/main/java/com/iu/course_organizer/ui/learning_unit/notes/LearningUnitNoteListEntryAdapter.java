package com.iu.course_organizer.ui.learning_unit.notes;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.database.model.LearningUnitNote;

import java.util.List;

public class LearningUnitNoteListEntryAdapter
        extends RecyclerView.Adapter<LearningUnitNoteListEntryAdapter.ViewHolder> {

    private final List<LearningUnitNote> learningUnitNotes;
    OnSaveToGoogleButtonClickListener saveToGoogleButtonClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView note;
        private final ImageView picture;
        private final Button btnSaveToGoogle;

        public ViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.learningUnitNote);
            picture = view.findViewById(R.id.learningUnitPicture);
            btnSaveToGoogle = view.findViewById(R.id.btnSaveToGoogle);
        }

        public TextView getNote() {
            return note;
        }

        public ImageView getPicture() {
            return picture;
        }

        public Button getBtnSaveToGoogle() {
            return btnSaveToGoogle;
        }
    }

    public LearningUnitNoteListEntryAdapter(List<LearningUnitNote> learningUnitNotes) {
        this.learningUnitNotes = learningUnitNotes;
    }

    public void setSaveToGoogleButtonListener(OnSaveToGoogleButtonClickListener buttonListener) {
        this.saveToGoogleButtonClickListener = buttonListener;
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

        String picturePath = learningUnitNotes.get(position).picturePath;
        if (StringUtils.isNotEmpty(picturePath)) {
            viewHolder.getNote().setVisibility(View.GONE);
            Bitmap picture = BitmapFactory.decodeFile(picturePath);
            viewHolder.getPicture().setImageBitmap(picture);
            viewHolder.getPicture().setMaxHeight(100);
            viewHolder.getBtnSaveToGoogle().setVisibility(View.VISIBLE);

            if (null != saveToGoogleButtonClickListener) {
                viewHolder.btnSaveToGoogle.setOnClickListener(
                        view -> saveToGoogleButtonClickListener.onClick(view, position));
            }
        } else {
            viewHolder.getPicture().setVisibility(View.GONE);
            viewHolder.getNote().setText(learningUnitNotes.get(position).note);
        }
    }

    public LearningUnitNote getByPosition(int position) {
        return learningUnitNotes.get(position);
    }

    @Override
    public int getItemCount() {
        return null == this.learningUnitNotes ? 0 : this.learningUnitNotes.size();
    }
}
