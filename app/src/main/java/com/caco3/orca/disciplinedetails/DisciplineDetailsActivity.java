package com.caco3.orca.disciplinedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.caco3.orca.R;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.ui.BaseActivity;
import com.caco3.orca.util.Preconditions;

/**
 * Activity where user can see details about {@link Discipline}.
 *
 * An {@link Intent} used to start this activity has contain an {@link #DISCIPLINE_EXTRA}.
 * Otherwise {@link IllegalStateException} will be thrown.
 * Use {@link #startForDiscipline(Context, Discipline)} method to start this activity
 *
 * @see #startForDiscipline(Context, Discipline)
 */
public class DisciplineDetailsActivity extends BaseActivity {
    private static final String FRAGMENT_TAG = "disc_details_frag";

    /**
     * An extra key for the {@link android.content.Intent} used to start this activity.
     * Will contain a {@link com.caco3.orca.orioks.model.Discipline} object that this activity
     * must to show details about
     */
    private static final String DISCIPLINE_EXTRA = "discipline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (getIntent().hasExtra(DISCIPLINE_EXTRA)) {
            Discipline discipline
                    = (Discipline)getIntent().getSerializableExtra(DISCIPLINE_EXTRA);

            // TODO: 12/20/16 create and host fragment
        } else {
            throw new IllegalStateException("intent used to start this activity doesn't contain '"
                    + DISCIPLINE_EXTRA + "' extra key. Did you run this activity via static method?");
        }
    }

    /**
     * Static method starts this activity
     * @param context to start activity
     * @param discipline to show details for
     * @throws NullPointerException <code>if context == null || discipline == null</code>
     */
    public static void startForDiscipline(Context context, Discipline discipline) {
        Preconditions.checkNotNull(context, "context == null");
        Preconditions.checkNotNull(discipline, "discipline == null");

        Intent intent = new Intent(context, DisciplineDetailsActivity.class);
        intent.putExtra(DISCIPLINE_EXTRA, discipline);
        context.startActivity(intent);
    }

    @Override
    protected boolean hasParentActivity() {
        /**
         * Parent activity is {@link com.caco3.orca.learning.LearningActivity}
         */
        return true;
    }
}
