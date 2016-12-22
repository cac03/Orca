package com.caco3.orca.disciplinedetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caco3.orca.R;
import com.caco3.orca.orioks.model.ControlEvent;
import com.caco3.orca.orioks.model.Teacher;
import com.caco3.orca.util.Preconditions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*package*/ class ControlEventsAdapter extends RecyclerView.Adapter<ControlEventsAdapter.ViewHolder> {

    /**
     * Callback
     */
    private OnControlEventClickedListener onControlEventClickedListener;
    /**
     * Items to show
     */
    private List<ControlEvent> items;

    /**
     * Where this adapter was created
     */
    private Context context;

    /**
     * Callback for the view that created this adapter.
     */
    /*package*/ interface OnControlEventClickedListener {
        /**
         * Called when user clicked an item in the list
         * @param controlEvent that was clicked
         */
        void onControlEventClicked(ControlEvent controlEvent);
    }

    /**
     * Constructs a new {@link ControlEventsAdapter} instance
     * @param context where this adapter was created
     * @param items to show
     * @param listener to receive callbacks
     * @throws NullPointerException if <code>context == null || items == null || listener == null</code>
     */
    /*package*/ ControlEventsAdapter(@NonNull Context context,
                                     @NonNull List<ControlEvent> items,
                                     @NonNull OnControlEventClickedListener listener){
        this.context = Preconditions.checkNotNull(context, "context == null");
        this.items = Preconditions.checkNotNull(items, "items == null");
        this.onControlEventClickedListener = Preconditions.checkNotNull(listener, "listener == null");
    }

    /**
     * Replaces all items currently showing with provided.
     * This method doesn't assign provided list to internal list.
     * It removes all elements from internal list and then adds all from provided.
     * @param items to replace with
     */
    public void setItems(List<ControlEvent> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discipline_details_control_event_item, parent, false);
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
        @BindView(R.id.control_event_achieved_points)
        TextView achievedPoints;

        @BindView(R.id.control_event_subject)
        TextView subjectText;

        @BindView(R.id.control_event_points_range)
        TextView pointsRangeText;

        @BindView(R.id.control_event_type_and_week)
        TextView typeAndWeekText;

        @BindView(R.id.control_event_entered_by)
        TextView enteredByText;

        /**
         * A decimal format that formats doubles and floats as int if possible and as float
         * if it's not possible
         * For example: 5.0 will be formatted as "5".
         * and 5.5 as "5.5"
         */
        private final NumberFormat numberFormat = new DecimalFormat("#.###");


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(final ControlEvent controlEvent) {
            typeAndWeekText.setText(context.getString(R.string.control_event_week_type_str_fmt,
                    controlEvent.getWeek(), controlEvent.getTypeName()));

            if (controlEvent.getTopic() == null || controlEvent.getTopic().trim().isEmpty()) {
                subjectText.setVisibility(View.GONE);
            } else {
                subjectText.setVisibility(View.VISIBLE);
                subjectText.setText(controlEvent.getTopic());
            }


            int pointsBgColorId;
            String achievedPointsText;
            if (!controlEvent.isAttended()) {
                pointsBgColorId = R.color.red;
                achievedPointsText = context.getString(R.string.control_event_not_attended);
            } else if (!controlEvent.isEntered()) {
                // no points achieved
                pointsBgColorId = android.R.color.darker_gray;
                achievedPointsText = context.getString(R.string.points_not_entered_value);
                enteredByText.setVisibility(View.GONE);
            } else {
                enteredByText.setVisibility(View.VISIBLE);
                float percentage
                        = controlEvent.getAchievedPoints() / controlEvent.getMaxAvailablePoints();

                if (percentage < .50) {
                    pointsBgColorId = R.color.red;
                } else if (percentage < .70) {
                    pointsBgColorId = R.color.yellow;
                } else if (percentage < .85) {
                    pointsBgColorId = R.color.green;
                } else {
                    pointsBgColorId = R.color.brightGreen;
                }
                achievedPointsText = numberFormat.format(controlEvent.getAchievedPoints());
            }
            achievedPoints.setText(achievedPointsText);
            achievedPoints.setBackgroundColor(ContextCompat.getColor(context, pointsBgColorId));


            pointsRangeText.setText(context.getString(R.string.control_event_points_range_str_fmt,
                    numberFormat.format(controlEvent.getMinPoints()),
                    numberFormat.format(controlEvent.getMaxAvailablePoints())));

            Teacher enteredBy = controlEvent.getEnteredBy();
            enteredByText.setText(enteredBy.getLastName() + " " + enteredBy.getFirstName() + " " +
                    enteredBy.getPatronymic());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onControlEventClickedListener.onControlEventClicked(controlEvent);
                }
            });
        }
    }
}
