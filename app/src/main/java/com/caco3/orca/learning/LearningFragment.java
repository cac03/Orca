package com.caco3.orca.learning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.caco3.orca.OrcaApp;
import com.caco3.orca.R;
import com.caco3.orca.login.LoginActivity;
import com.caco3.orca.orioks.model.Discipline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LearningFragment extends Fragment
        implements LearningView,
        SwipeRefreshLayout.OnRefreshListener,
        DisciplinesAdapter.OnItemClickedListener {

    @BindView(R.id.learning_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.learning_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    /**
     * Adapter associated with {@link #recyclerView}
     */
    private DisciplinesAdapter adapter;

    @Inject
    LearningPresenterImpl presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.learning_fragment, container, false);
        ButterKnife.bind(this, root);
        refreshLayout.setOnRefreshListener(this);

        adapter = new DisciplinesAdapter(getContext(),
                this, // on item clicked listener
                new ArrayList<Discipline>());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        DaggerLearningComponent.builder()
                .applicationComponent(OrcaApp.get(getContext()).getApplicationComponent())
                .build()
                .inject(this);

        super.onViewCreated(view, savedInstanceState);
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
    public void onItemClicked(Discipline discipline) {
        presenter.onDisciplineClicked(discipline);
    }

    @Override
    public void setItems(List<Discipline> disciplineList) {
        adapter.setItems(disciplineList);
    }

    @Override
    public void sayIncorrectLoginOrPassword() {
        Snackbar.make(getView(), R.string.login_or_password_incorrect, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.reenter_login_and_password, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onReLogInRequest();
                    }
                });
    }

    @Override
    public void sayNetworkErrorOccurred() {
        Toast.makeText(getContext(), R.string.network_error_occurred, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        refreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void setPresenter(LearningPresenter presenter) {
        this.presenter = (LearningPresenterImpl) presenter;
    }

    @Override
    public void navigateToDisciplineDetailsActivity(Discipline discipline) {
        Toast.makeText(getContext(), "Navigating to discipline details activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLoginActivity() {
        Activity activity = getActivity();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }
}
