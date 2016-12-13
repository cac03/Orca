package com.caco3.orca.schedule;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caco3.orca.R;
import com.caco3.orca.schedule.model.DaySchedule;
import com.caco3.orca.schedule.model.Lesson;
import com.caco3.orca.util.Preconditions;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*package*/ class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private static final String TIME_TEACHER_STR_FMT
            = "%s - %s, %s";
    private static final String TIME_STR_FMT = "%s - %s";

    private final DateFormat timeFormat;

    private List<DaySchedule> items;

    private Context context;

    /**
     * If lesson has passed we will make it transparent with this alpha-channel value
     */
    private static final float PASSED_LESSON_ALPHA = 0.4f;

    /*package*/ ScheduleAdapter(Context context, List<DaySchedule> items) {
        this.items = Preconditions.checkNotNull(items);
        this.context = Preconditions.checkNotNull(context);
        this.timeFormat = android.text.format.DateFormat.getTimeFormat(context);
    }

    /*package*/ void setItems(List<DaySchedule> items) {
        Preconditions.checkNotNull(items);

        while (!this.items.isEmpty()) {
            this.items.remove(0);
            notifyItemRemoved(0);
        }

        for(int i = 0; i < items.size(); i++) {
            this.items.add(items.get(i));
            notifyItemInserted(i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /*package*/ class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.schedule_lessons_root)
        LinearLayout lessonsRoot;

        @BindView(R.id.schedule_item_date)
        TextView dateTextView;

        /*package*/ ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }


        /*package*/ void bind(DaySchedule item) {
            CharSequence dateString = DateUtils.getRelativeTimeSpanString(
                    item.getTimeMillis(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
            dateTextView.setText(dateString);

            lessonsRoot.removeAllViews();
            if (item.getLessons() != null) {
                for (int i = 0; i < item.getLessons().size(); i++) {
                    Lesson lesson = item.getLessons().get(i);
                    lessonsRoot.addView(new LessonViewHolder().inflateAndBind(i, lesson));
                }
            }
        }


        /*package*/ class LessonViewHolder {

            @BindView(R.id.schedule_lesson_item_discipline_name)
            TextView disciplineName;

            @BindView(R.id.schedule_lesson_item_time_teacher)
            TextView beginEndTimeTeacher;

            @BindView(R.id.schedule_lesson_item_ordinal_number)
            TextView ordinalNumber;

            @BindView(R.id.schedule_lesson_item_classroom)
            TextView classroom;

            /*package*/ View inflateAndBind(int ordinal, Lesson lesson) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.schedule_lesson_item, (ViewGroup)itemView, false);
                ButterKnife.bind(this, view);

                ordinalNumber.setText((ordinal + 1) + "");

                String beginTimeStr = timeFormat.format(new Date(lesson.getBeginMillis()));
                String endTimeStr = timeFormat.format(new Date(lesson.getEndMillis()));

                if (!lesson.isNoLesson()) {
                    disciplineName.setText(lesson.getDisciplineName());
                    beginEndTimeTeacher.setText(String.format(TIME_TEACHER_STR_FMT,
                                    beginTimeStr,
                                    endTimeStr,
                                    lesson.getTeacherName()));

                   classroom.setText(lesson.getClassroom());
                } else {
                    disciplineName.setText(context.getString(R.string.no_lesson));
                    beginEndTimeTeacher.setText(String.format(TIME_STR_FMT,
                                    beginTimeStr,
                                    endTimeStr));
                }

                if (lesson.getEndMillis() < System.currentTimeMillis()) {
                    ordinalNumber.setAlpha(PASSED_LESSON_ALPHA);
                    disciplineName.setAlpha(PASSED_LESSON_ALPHA);
                    beginEndTimeTeacher.setAlpha(PASSED_LESSON_ALPHA);
                    classroom.setAlpha(PASSED_LESSON_ALPHA);
                }

                return view;
            }
        }

    }


}
