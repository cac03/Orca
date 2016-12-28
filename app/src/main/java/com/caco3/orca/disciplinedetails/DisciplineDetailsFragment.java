package com.caco3.orca.disciplinedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caco3.orca.OrcaApp;
import com.caco3.orca.R;
import com.caco3.orca.orioks.model.ControlEvent;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.Teacher;
import com.caco3.orca.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisciplineDetailsFragment extends Fragment
        implements DisciplineDetailsView,
        ControlEventsAdapter.OnControlEventClickedListener {
    /**
     * A key for {@link Bundle} that will contain {@link Discipline} object
     * this fragment shows details for.
     * @see #getDiscipline()
     * @see #forDiscipline(Discipline)
     */
    private static final String DISCIPLINE_ARG = "disc";

    /**
     * Associated with {@link #recyclerView}
     */
    private ControlEventsAdapter adapter;

    /**
     * Shows {@link Discipline#getAttachedControlEvents()}
     */
    @BindView(R.id.discipline_details_recycler_view)
    RecyclerView recyclerView;

    /**
     * Presenter associated with this view.
     * Not <code>null</code>. Injected in {@link #onViewCreated(View, Bundle)}
     */
    @Inject
    DisciplineDetailsPresenterImpl presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.discipline_details_frag, container, false);
        ButterKnife.bind(this, root);

        // setup recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ControlEventsAdapter(getContext(),
                new ArrayList<ControlEvent>(),
                this/**{@link ControlEventsAdapter#OnControlEventClickedListener}*/);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                layoutManager.getOrientation()));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (presenter == null) {
            OrcaApp.get(getContext())
                    .getDisciplineDetailsComponent()
                    .inject(this);
        }
        presenter.onViewAttached(this);
    }

    @Override
    public void onDestroyView() {
        presenter.onViewDetached();
        super.onDestroyView();
    }

    /**
     * Creates new {@link DisciplineDetailsFragment} instance and adds provided
     * {@link Discipline} object to arguments
     * @param discipline to show details for
     * @return {@link DisciplineDetailsFragment} with set arguments
     * @throws NullPointerException if <code>discipline == null</code>
     */
    public static DisciplineDetailsFragment forDiscipline(Discipline discipline) {
        Preconditions.checkNotNull(discipline, "discipline == null");

        Bundle args = new Bundle(1);
        args.putSerializable(DISCIPLINE_ARG, discipline);
        DisciplineDetailsFragment fragment = new DisciplineDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public Discipline getDiscipline() {
        return (Discipline)getArguments().getSerializable(DISCIPLINE_ARG);
    }

    @Override
    public void setControlEvents(List<ControlEvent> controlEvents) {
        adapter.setItems(controlEvents);
    }

    @Override
    public void setTeachers(List<Teacher> teachers) {
    }

    @Override
    public void setDepartment(String department) {
    }

    @Override
    public void setAssessmentType(String assessmentType) {
    }

    @Override
    public void setAchievedPoints(float achievedPoints) {
    }

    @Override
    public void setAvailablePoints(float availablePoints) {
    }

    @Override
    public void setPresenter(DisciplineDetailsPresenter presenter) {
    }

    @Override
    public void onControlEventClicked(ControlEvent controlEvent) {
        presenter.onControlEventClicked(controlEvent);
    }
}
