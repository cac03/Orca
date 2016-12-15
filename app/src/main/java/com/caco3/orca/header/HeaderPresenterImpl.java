package com.caco3.orca.header;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.miet.MietUtils;

import java.util.Calendar;

import javax.inject.Inject;


/*package*/ class HeaderPresenterImpl implements HeaderPresenter {

    private OrioksRepository repository;
    private CredentialsManager credentialsManager;

    @Inject
    HeaderPresenterImpl(OrioksRepository repository, CredentialsManager credentialsManager) {
        this.repository = repository;
        this.credentialsManager = credentialsManager;
    }

    @Override
    public void onViewAttached(HeaderView view) {
        int weekOfSemester = MietUtils.getWeekOfSemester(Calendar.getInstance());
        if (weekOfSemester != -1) {
            view.showLearningWeek(weekOfSemester);
        } else {
            // holidays or exam period...
            // TODO: 12/15/16 specify
            view.showExamPeriod();
        }

        view.showStudent(repository.getStudent(credentialsManager.getCurrentCredentials()));
    }

    @Override
    public void onViewDetached() {

    }
}