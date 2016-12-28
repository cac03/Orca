package com.caco3.orca.schedule;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.caco3.orca.OrcaApp;
import com.caco3.orca.R;
import com.caco3.orca.schedule.model.DaySchedule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleFragment extends Fragment
        implements ScheduleView,
        SwipeRefreshLayout.OnRefreshListener,
        SelectGroupDialog.OnGroupSelected {

    /**
     * View with main content.
     * It is a view the user must see the most time in this fragment.
     * Consist of:
     * {@link #refreshLayout}, which allows to reload a schedule for current group
     * {@link #recyclerView}, which displays schedule
     */
    @BindView(R.id.schedule_main_content_view)
    View mainContentView;

    @BindView(R.id.schedule_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.schedule_recycler_view)
    RecyclerView recyclerView;

    /**
     * Error view.
     * There are two error views displayed with this view.
     * First is 'could not load groups' view.
     * The second is 'could not load schedule' view.
     * Consist of:
     * {@link #errorRetryBtn} which allows to retry load *something*, depending on {@link #displayedView}
     * @see #retryBtnClicked()
     *
     * {@link #errorTextView} where the error is described
     */
    @BindView(R.id.schedule_error_view)
    View errorView;

    /**
     * 'Retry' button.
     */
    @BindView(R.id.schedule_retry_btn)
    Button errorRetryBtn;

    /**
     * An text view that describes error
     */
    @BindView(R.id.schedule_error_text)
    TextView errorTextView;

    /**
     * Progress bar. Shown on the whole screen hiding {@link #currentView}
     */
    @BindView(R.id.schedule_progress_bar)
    ProgressBar progressBar;

    /**
     * No group selected view.
     * Shown when there is no group selected to show schedule for.
     * Consist of:
     * {@link #selectGroupSpinner}
     * @see #showNoGroupSelectedView(List)
     *
     * {@link #groupSelectedBtn} when user has selected group, he clicks this button
     * @see #groupSelectedBtnClicked()
     */
    @BindView(R.id.schedule_select_group_view)
    View noGroupSelectedView;

    @BindView(R.id.schedule_select_group_spinner)
    Spinner selectGroupSpinner;

    @BindView(R.id.schedule_group_selected_btn)
    Button groupSelectedBtn;

    /**
     * Reference for current view we showing.
     * One of these values:
     * {@link #mainContentView}
     * {@link #errorView}
     * {@link #noGroupSelectedView}
     */
    private View currentView;

    /**
     * The view that is currently displayed
     */
    private enum DisplayedView {
        /**@see #showScheduleView(String) */
        MAIN_CONTENT,
        /**@see #showCouldNotLoadGroupsErrorView() */
        COULD_NOT_LOAD_GROUPS_ERROR,
        /**@see #showCouldNotGetScheduleErrorView(String) */
        COULD_NOT_LOAD_SCHEDULE_ERROR,
        /** @see #showNoGroupSelectedView(List) */
        NO_GROUP_SELECTED
    }

    private DisplayedView displayedView;

    /**
     * Attached to {@link #recyclerView}
     */
    private ScheduleAdapter adapter;

    /**
     * Presenter associated with this view.
     * Not null. Injected in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Inject
    SchedulePresenter presenter;

    /**
     * Decorator for {@link #recyclerView} adds bottom margin and left, right if there is more than 1 item per row
     * So the left item hasn't left margin and the right hasn't right margin
     */
    private final RecyclerView.ItemDecoration marginDecorator = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int horizontalMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
            int bottomMargin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);

            int itemsPerRow = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int itemPosition = parent.getChildAdapterPosition(view);
            if (itemsPerRow == 1){
                // no need margins
                outRect.set(0, 0, 0, bottomMargin);
            } else if (itemPosition % itemsPerRow == 0) {
                // top left item
                // set only right margin
                outRect.set(0, 0, horizontalMargin / 2, bottomMargin);
            } else if ((itemPosition + 1) % itemsPerRow == 0) {
                // top right
                // only left margin
                outRect.set(horizontalMargin / 2, 0, 0, bottomMargin);
            } else {
                // between top left and top right in the row
                // set both
                outRect.set(horizontalMargin / 2, 0, horizontalMargin / 2, bottomMargin);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_fragment, container, false);
        ButterKnife.bind(this, root);

        displayedView = DisplayedView.MAIN_CONTENT;
        currentView = mainContentView;

        adapter = new ScheduleAdapter(getContext(), new ArrayList<DaySchedule>());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(marginDecorator);
        refreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewVisible();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter == null) {
            OrcaApp.get(getContext())
                    .getScheduleComponent()
                    .inject(this);
        }
        presenter.onViewAttached(this);
    }

    @Override
    public void onDestroyView() {
        presenter.onViewDetached();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        presenter.onRefreshRequest();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        refreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void showNoGroupSelectedView(List<String> list) {
        final String[] groupNames = list.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, groupNames);
        selectGroupSpinner.setAdapter(adapter);

        currentView.setVisibility(View.GONE);
        noGroupSelectedView.setVisibility(View.VISIBLE);
        currentView = noGroupSelectedView;
        displayedView = DisplayedView.NO_GROUP_SELECTED;
    }

    @Override
    public void showCouldNotLoadGroupsErrorView() {
        currentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.could_not_load_groups_error);
        currentView = errorView;
        displayedView = DisplayedView.COULD_NOT_LOAD_GROUPS_ERROR;
    }

    @Override
    public void showCouldNotGetScheduleErrorView(String groupName) {
        currentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorTextView.setText(getString(R.string.could_not_load_schedule_for_group_error, groupName));
        currentView = errorView;
        displayedView = DisplayedView.COULD_NOT_LOAD_SCHEDULE_ERROR;
    }

    @Override
    public void showScheduleView(String groupName) {
        currentView.setVisibility(View.GONE);
        mainContentView.setVisibility(View.VISIBLE);
        currentView = mainContentView;
        getActivity().setTitle(groupName);
    }

    @Override
    public void showSchedule(List<DaySchedule> schedule) {
        adapter.setItems(schedule);
    }

    @Override
    public void openSelectGroupDialog(final List<String> groupNames) {
        DialogFragment dialogFragment;
        if (groupNames instanceof ArrayList) {
             dialogFragment = SelectGroupDialog.with((ArrayList<String>)groupNames);
        } else {
            dialogFragment = SelectGroupDialog.with(new ArrayList<>(groupNames));
        }
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "ignored");
    }

    @Override
    public void showProgress() {
        currentView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        currentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(SchedulePresenter presenter) {

    }

    @Override
    public void sayNoNetwork() {
        Toast.makeText(getContext(), R.string.no_network_check_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sayNetworkErrorOccurred() {
        Toast.makeText(getContext(), R.string.network_error_occurred, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sayParseErrorOccurred() {
        Toast.makeText(getContext(), R.string.could_not_parse_response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGroupSelected(String selectedGroupName) {
        presenter.onGroupSelected(selectedGroupName);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.schedule_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.schedule_change_group_menu_item:
                presenter.onChangeGroupRequest();
                return true;
            default:
                return false;
        }
    }

    @OnClick(R.id.schedule_group_selected_btn)
    /*package*/ void groupSelectedBtnClicked(){
        int i = selectGroupSpinner.getSelectedItemPosition();
        String selected = (String)selectGroupSpinner.getAdapter().getItem(i);
        presenter.onGroupSelected(selected);
    }

    @OnClick(R.id.schedule_retry_btn)
    /*package*/ void retryBtnClicked(){
        if (displayedView == DisplayedView.COULD_NOT_LOAD_SCHEDULE_ERROR) {
            presenter.retryToLoadSchedule();
        } else {
            presenter.retryLoadGroups();
        }
    }

}
