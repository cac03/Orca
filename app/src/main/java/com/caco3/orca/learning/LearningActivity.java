package com.caco3.orca.learning;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.caco3.orca.R;
import com.caco3.orca.ui.BaseActivity;

/**
 * This activity hosts {@link LearningFragment}
 * @see LearningFragment
 * @see LearningView
 * @see LearningPresenter
 * @see LearningPresenterImpl
 */
public class LearningActivity extends BaseActivity {
    private static final String FRAGMENT_TAG = "learning_frag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new LearningFragment();
            fragment.setRetainInstance(true);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_TAG)
                .commitNow();
    }
}
