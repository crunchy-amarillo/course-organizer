package com.iu.course_organizer.ui.course.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.course_organizer.R;
import com.iu.course_organizer.database.model.Course;

import java.util.List;

public class CourseListEntryAdapter extends RecyclerView.Adapter<CourseListEntryAdapter.ViewHolder> {

    private final List<Course> courses;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView session;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.courseListTitle);
            description = view.findViewById(R.id.courseListDescription);
            session = view.findViewById(R.id.courseListSession);
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getSession() {
            return session;
        }
    }

    public CourseListEntryAdapter(List<Course> courses) {
        this.courses = courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses.clear();
        this.courses.addAll(courses);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.course_list_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getDescription().setText(courses.get(position).description);
        viewHolder.getTitle().setText(courses.get(position).title);
        viewHolder.getSession().setText(courses.get(position).session.toString());
    }

    public Course getByPosition(int position)
    {
        return courses.get(position);
    }

    @Override
    public int getItemCount() {
        return null == this.courses ? 0 : this.courses.size();
    }
}
