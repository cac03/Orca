package com.caco3.orca.header;

import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.miet.MietUtils;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Student;

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

        UserCredentials credentials = credentialsManager.getActive();
        if (credentials != null) {
            Student student = repository.getStudent(credentials);
            if (student != null) {
                view.showStudent(student);
            }
        }
    }

    @Override
    public void onViewDetached() {

    }
}
