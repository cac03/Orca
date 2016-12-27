package com.caco3.orca.notifications;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.app.NotificationCompat;

import com.caco3.orca.R;
import com.caco3.orca.orioks.model.ControlEvent;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete {@link NotificationsMaker} implementation which creates notifications about points
 * changes in the {@link ControlEvent}s
 */
public class PointsChangedNotificationsMaker implements NotificationsMaker {

    @DrawableRes
    private static final int SMALL_ICON_ID = R.drawable.ic_orca_logo;

    private final Context context;
    private final OrioksResponse oldResponse;
    private final OrioksResponse newResponse;

    /**
     * @param context any
     * @param oldResponse to compare
     * @param newResponse to compare
     *
     * @throws NullPointerException if <code>context == null ||
     * oldResponse == null || newResponse == null</code>
     */
    public PointsChangedNotificationsMaker(Context context,
                                           OrioksResponse oldResponse,
                                           OrioksResponse newResponse) {
        this.context = Preconditions.checkNotNull(context, "context == null");
        this.oldResponse = Preconditions.checkNotNull(oldResponse, "oldResponse == null");
        this.newResponse = Preconditions.checkNotNull(newResponse, "newResponse == null");
    }

    @Override
    public List<Notification> makeNotifications() {
        List<Notification> notifications = new ArrayList<>();
        for(Discipline oldDiscipline : oldResponse.getDisciplines()) {
            for(Discipline newDiscipline : newResponse.getDisciplines()) {
                if (oldDiscipline.getName().equals(newDiscipline.getName())) {
                    for(ControlEvent oldEvent : oldDiscipline.getAttachedControlEvents()) {
                        for (ControlEvent newEvent : newDiscipline.getAttachedControlEvents()) {
                            if (areControlEventsSameExceptPoints(oldEvent, newEvent)
                                    && arePointsChanged(oldEvent, newEvent)) {
                                notifications.add(makeNotification(newDiscipline, oldEvent, newEvent));
                            }
                        }
                    }
                }
            }
        }

        return notifications;
    }

    /**
     * Tests whether two control events are same excluding checking for entered points and entered by teacher
     * @param first control event
     * @param second control event
     * @return boolean
     */
    private boolean areControlEventsSameExceptPoints(ControlEvent first, ControlEvent second) {
        return first.getTypeName().equals(second.getTypeName())
                && first.getWeek() == second.getWeek()
                && first.getTopic().equals(second.getTopic());
    }

    /**
     * Tests whether entered points of two control events are not the same
     * @param first control event
     * @param second control event
     * @return true if entered points of 2 control events are not the same
     */
    private boolean arePointsChanged(ControlEvent first, ControlEvent second) {
        return Float.compare(first.getAchievedPoints(), second.getAchievedPoints()) != 0;
    }

    private Notification makeNotification(Discipline discipline,
                                          ControlEvent oldEvent,
                                          ControlEvent newEvent) {
        return new NotificationCompat.Builder(context)
                .setContentTitle(discipline.getName())
                .setSmallIcon(SMALL_ICON_ID)
                .setContentText(makeContentString(oldEvent, newEvent))
                .build();
    }

    private String makeContentString(ControlEvent oldEvent, ControlEvent newEvent) {
        return context.getString(R.string.notification_new_points_content,
                newEvent.getWeek(),
                newEvent.getTypeName(),
                newEvent.getAchievedPoints(),
                newEvent.getMaxAvailablePoints());
    }
}
