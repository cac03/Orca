package com.caco3.orca.orioksautoupdate;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.caco3.orca.OrcaApp;
import com.caco3.orca.credentials.CredentialsManager;
import com.caco3.orca.data.orioks.OrioksRepository;
import com.caco3.orca.notifications.PointsChangedNotificationsMaker;
import com.caco3.orca.orioks.Orioks;
import com.caco3.orca.orioks.OrioksException;
import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.util.Preconditions;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.exceptions.Exceptions;
import timber.log.Timber;

public class OrioksAutoUpdateService extends IntentService {

    @Inject
    Orioks orioks;

    @Inject
    OrioksRepository orioksRepository;

    @Inject
    CredentialsManager credentialsManager;

    public OrioksAutoUpdateService(){
        super("OrioksAutoUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.i("Running");
        injectFields();

        final UserCredentials credentials = credentialsManager.getActive();
        if (credentials != null) {
            orioks.getResponseForCurrentSemester(credentials)
                    .subscribe(new ResponseReceivedSubscriber(credentials));
        } else {
            Timber.w("Attempt to update data. But there is no active credentials");
        }
    }

    private void injectFields(){
        OrcaApp.get(this)
                .getOrioksAutoUpdateComponent()
                .inject(this);
    }

    private class ResponseReceivedSubscriber extends Subscriber<OrioksResponse> {

        private final UserCredentials credentials;

        private ResponseReceivedSubscriber(UserCredentials credentials) {
            this.credentials = Preconditions.checkNotNull(credentials, "credentials == null");
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "An error occurred while receiving OrioksResponse");
            if (e instanceof OrioksException) {
                Timber.e("Orioks returned error: %s", ((OrioksException)e).getOrioksErrorMessage());
            } else if (e instanceof IOException) {
                Timber.e("No network?");
            } else {
                throw Exceptions.propagate(e);
            }
        }

        @Override
        public void onNext(OrioksResponse response) {
            Timber.i("Successfully received response");

            List<Notification> notifications = getNotifications(response);
            fireNotifications(notifications);
            saveResponse(credentials, response);

        }
    }

    private List<Notification> getNotifications(OrioksResponse response) {
        UserCredentials credentials = credentialsManager.getActive();
        List<Discipline> oldList = orioksRepository
                .getDisciplines(credentials, orioksRepository.getCurrentSemester(credentials));
        List<Discipline> newList = response.getDisciplines();

        if (oldList == null) {
            return Collections.emptyList();
        } else {
            return new PointsChangedNotificationsMaker(this, oldList, newList)
                    .makeNotifications();
        }
    }

    private void fireNotifications(Collection<Notification> notifications) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        for(Notification notification : notifications) {
            notificationManager.notify(0, notification);
        }
    }

    private void saveResponse(UserCredentials credentials, OrioksResponse response) {
        orioksRepository.saveDisciplines(credentials, response.getDisciplines(), response.getCurrentSemester());
        orioksRepository.saveStudent(credentials, response.getStudent());
    }
}
