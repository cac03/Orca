package com.caco3.orca.learning;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caco3.orca.R;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.util.Preconditions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for {@link Discipline} items.
 * Uses layout {@link com.caco3.orca.R.layout#learning_discipline_item}
 * Used in {@link LearningFragment}
 */
/*package*/ class DisciplinesAdapter extends RecyclerView.Adapter<DisciplinesAdapter.ViewHolder> {

    /**
     * Callback to notify the listener that item was clicked
     */
    /*package*/interface OnItemClickedListener {
        /**
         * Called when item was clicked
         * @param discipline {@link Discipline} object associated with clicked view
         */
        void onItemClicked(Discipline discipline);
    }

    /**
     * Where this adapter was created
     */
    private Context context;

    /**
     * Listens 'click' events
     */
    private OnItemClickedListener listener;

    /**
     * Items to display
     */
    private List<Discipline> items;

    /*package*/ DisciplinesAdapter(Context context, OnItemClickedListener listener, List<Discipline> items) {
        this.context = Preconditions.checkNotNull(context);
        this.listener = Preconditions.checkNotNull(listener);
        this.items = Preconditions.checkNotNull(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.learning_discipline_item, parent, false);
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

    /*package*/ void setItems(List<Discipline> disciplines) {
        items.clear();
        items.addAll(disciplines);
        notifyDataSetChanged();
    }

    /*package*/class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.discipline_name)
        TextView disciplineName;

        @BindView(R.id.discipline_points)
        TextView disciplinePoints;

        @BindView(R.id.discipline_assessment_type)
        TextView assessmentType;

        private final NumberFormat numberFormat = new DecimalFormat("#.###");

        /*package*/ ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /*package*/ void bind(Discipline discipline) {
            disciplineName.setText(discipline.getName());

            float achievedPoints = discipline.getTotalAchievedPoints();
            float availablePoints = discipline.getTotalAvailablePoints();
            String pointsText;
            int pointsBgColorId;
            if (Float.compare(availablePoints, 0.0f) == 0) {
                // no points earned
                pointsBgColorId = android.R.color.darker_gray;
                pointsText = "-";
            } else {
                float percentage = discipline.getTotalAchievedPoints() / discipline.getTotalAvailablePoints();

                if (percentage < .50) {
                    pointsBgColorId = R.color.red;
                } else if (percentage < .75) {
                    pointsBgColorId = R.color.yellow;
                } else if (percentage < .85) {
                    pointsBgColorId = R.color.green;
                } else {
                    pointsBgColorId = R.color.brightGreen;
                }

                pointsText = numberFormat.format(achievedPoints);

            }
            disciplinePoints.setText(pointsText);

            disciplinePoints.setBackgroundColor(ResourcesCompat
                    .getColor(context.getResources(), pointsBgColorId, null));

            assessmentType.setText(discipline.getAssessmentType());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(items.get(getAdapterPosition()));
                }
            });

        }
    }
}
