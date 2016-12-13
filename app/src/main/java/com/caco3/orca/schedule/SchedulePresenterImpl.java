package com.caco3.orca.schedule;

import com.caco3.orca.data.schedule.ScheduleRepository;
import com.caco3.orca.miet.MietUtils;
import com.caco3.orca.schedule.model.DaySchedule;
import com.caco3.orca.schedule.model.Lesson;
import com.caco3.orca.schedule.model.ScheduleHelper;
import com.caco3.orca.scheduleapi.ScheduleApi;
import com.caco3.orca.scheduleapi.ScheduleItem;
import com.caco3.orca.util.Preconditions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/*package*/ class SchedulePresenterImpl implements SchedulePresenter {

    private enum State {
        SHOWING_SCHEDULE,
        LOADING_GROUPS,
        LOADING_GROUPS_FAILED,
        LOADING_SCHEDULE,
        LOADING_SCHEDULE_FAILED,
        NO_GROUP_SELECTED,
        REFRESHING_SCHEDULE
    }

    private State state;

    private ScheduleView view;

    // not null, injected in c-tor
    private ScheduleApi scheduleApi;

    // not null, injected in c-tor
    private ScheduleRepository repository;

    private SchedulePreferences preferences;

    private static final String SEPARATOR = "/";

    private final Calendar today = getTodayCalendar();

    @Inject
    /*package*/ SchedulePresenterImpl(ScheduleApi scheduleApi,
                                      ScheduleRepository repository,
                                      SchedulePreferences schedulePreferences) {
        Timber.d("new instance");
        this.scheduleApi = scheduleApi;
        this.repository = repository;
        this.preferences = schedulePreferences;
    }

    @Override
    public void onRefreshRequest() {
        state = State.REFRESHING_SCHEDULE;
        loadSchedule().subscribe(new ScheduleRefreshedSubscriber());
    }

    @Override
    public void onViewAttached(ScheduleView scheduleView) {
        Timber.d("onViewAttached()");
        this.view = scheduleView;
        if (state != null) {
            applyStateToView();
        } else {
            final String groupName = preferences.getGroupToShowScheduleFor();
            if (groupName == null) {
                List<String> groupNames = repository.getGroupNames();
                if (groupNames == null) {
                    state = State.LOADING_GROUPS;
                    if (view != null) {
                        view.showProgress();
                    }
                    loadGroups().subscribe(new GroupsLoadedSubscriber());

                } else {
                    state = State.NO_GROUP_SELECTED;
                    if (view != null) {
                        view.showNoGroupSelectedView(groupNames);
                    }
                }
            } else {
                if (repository.getSchedule(groupName).isEmpty()) {
                    if (view != null) {
                        view.showProgress();
                    }
                    state = State.LOADING_SCHEDULE;
                    loadSchedule().subscribe(new ScheduleLoadedSubscriber());
                } else {
                    state = State.SHOWING_SCHEDULE;
                    view.showScheduleView(groupName);
                    List<Lesson> schedule = repository.getSchedule(groupName, System.currentTimeMillis());
                    view.showSchedule(ScheduleHelper.buildDailySchedule(schedule, SEPARATOR));
                }
            }
        }
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onGroupSelected(final String groupName) {
        preferences.setGroupToShowScheduleFor(groupName);
        if (view != null) {
            view.showProgress();
        }
        state = State.LOADING_SCHEDULE;
        loadSchedule().subscribe(new ScheduleLoadedSubscriber());
    }

    @Override
    public void onChangeGroupRequest(){
        if (view != null) {
            view.openSelectGroupDialog(repository.getGroupNames());
        }
    }

    @Override
    public void onViewVisible() {
        Timber.d("view became visible");
        Calendar calendar = getTodayCalendar();
        if (calendar.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) != 0) {
            // 'this.today' is not today
            this.today.setTimeInMillis(calendar.getTimeInMillis());
            // invalidate view since it may contain data for past time
            String groupName = preferences.getGroupToShowScheduleFor();
            if (groupName != null) {
                retrieveSchedule(groupName, today.getTimeInMillis())
                        .subscribe(new ScheduleReceivedSubscriber(groupName));
            } // else not our business
        }
    }

    @Override
    public void retryLoadGroups() {
        state = State.LOADING_GROUPS;
        loadGroups().subscribe(new GroupsLoadedSubscriber());
    }

    @Override
    public void retryToLoadSchedule() {
        state = State.LOADING_SCHEDULE;
        loadSchedule().subscribe(new ScheduleLoadedSubscriber());
    }

    private Observable<List<String>> loadGroups(){
        return scheduleApi.getGroups()
                .subscribeOn(Schedulers.io())
                .map(new Func1<Set<String>, List<String>>() {
                    @Override
                    public List<String> call(Set<String> strings) {
                        repository.saveGroupNames(strings);
                        return repository.getGroupNames();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<DaySchedule>> loadSchedule(){
        final String group = preferences.getGroupToShowScheduleFor();
        return scheduleApi.getSchedule(group)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Set<ScheduleItem>, List<DaySchedule>>() {
                    @Override
                    public List<DaySchedule> call(Set<ScheduleItem> scheduleItems) {
                        long begin = MietUtils.getCurrentSemesterBeginTime();
                        long end = MietUtils.getCurrentSemesterEndTime();
                        List<Lesson> schedule = ScheduleHelper.buildSchedule(scheduleItems, begin, end);
                        repository.saveSchedule(schedule, group);
                        return ScheduleHelper.buildDailySchedule(schedule, System.currentTimeMillis(), end, SEPARATOR);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private class GroupsLoadedSubscriber extends Subscriber<List<String>> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            state = State.LOADING_GROUPS_FAILED;
            Timber.e(e, "An error occurred while loading groups");
            if (e instanceof IOException) {
                if (view != null) {
                    view.showCouldNotLoadGroupsErrorView();
                    view.sayNetworkErrorOccurred();
                }
            } else {
                throw Exceptions.propagate(e);
            }
        }

        @Override
        public void onNext(List<String> list) {
            state = State.NO_GROUP_SELECTED;
            if (view != null) {
                view.hideProgress();
                view.showNoGroupSelectedView(list);
            }
        }
    }

    private class ScheduleLoadedSubscriber extends Subscriber<List<DaySchedule>> {
        final String group = preferences.getGroupToShowScheduleFor();

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            state = State.LOADING_SCHEDULE_FAILED;
            Timber.e(e, "An error occurred while loading schedule for: %s", group);
            if (e instanceof IOException) {
                if (view != null) {
                    view.hideProgress();
                    view.showCouldNotGetScheduleErrorView(group);
                }
            } else {
                throw Exceptions.propagate(e);
            }
        }

        @Override
        public void onNext(List<DaySchedule> schedules) {
            state = State.SHOWING_SCHEDULE;
            if (view != null) {
                view.hideProgress();
                view.showScheduleView(group);
                view.showSchedule(schedules);
            }
        }
    }

    private class ScheduleRefreshedSubscriber extends Subscriber<List<DaySchedule>> {
        final String group = preferences.getGroupToShowScheduleFor();

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "An error occurred while loading schedule for: %s", group);
            if (e instanceof IOException) {
                if (view != null) {
                    view.sayNetworkErrorOccurred();
                }
            } else {
                throw Exceptions.propagate(e);
            }
        }

        @Override
        public void onNext(List<DaySchedule> schedules) {
            state = State.SHOWING_SCHEDULE;
            if (view != null) {
                view.setRefreshing(false);
                view.showScheduleView(group);
                view.showSchedule(schedules);
            }
        }
    }

    private Observable<List<DaySchedule>> retrieveSchedule(final String group, final long from) {
        return Observable.fromCallable(new Callable<List<Lesson>>() {
                    @Override
                    public List<Lesson> call() {
                        return repository.getSchedule(group, from);
                    }
                })
                .map(new Func1<List<Lesson>, List<DaySchedule>>() {
                    @Override
                    public List<DaySchedule> call(List<Lesson> lessons) {
                        return ScheduleHelper.buildDailySchedule(lessons, SEPARATOR);
                    }
                });
    }

    private String getCurrentGroup(){
        return preferences.getGroupToShowScheduleFor();
    }

    private class ScheduleReceivedSubscriber extends Subscriber<List<DaySchedule>> {
        private final String forGroup;

        ScheduleReceivedSubscriber(String forGroup) {
            this.forGroup = forGroup;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "An error occurred while retrieving schedule");
            if (e instanceof IOException) {
                if (view != null) {
                    if (getCurrentGroup().equals(forGroup)) {
                        view.hideProgress();
                        view.showCouldNotGetScheduleErrorView(forGroup);
                        view.sayNetworkErrorOccurred();
                    }
                    /**
                     * Else another subscriber will do its work
                     */
                }
            }
        }

        @Override
        public void onNext(List<DaySchedule> daySchedule) {
            if (view != null) {
                if (getCurrentGroup().equals(forGroup)) {
                    view.hideProgress();
                    view.showScheduleView(forGroup);
                    view.showSchedule(daySchedule);
                }
                /**
                 * else another subscriber will do its work
                 */
            }
        }
    }

    private void applyStateToView() {
        Preconditions.checkNotNull(state, "Attempt to apply state to view. But state == null");
        switch (state) {
            case SHOWING_SCHEDULE:
                String group = preferences.getGroupToShowScheduleFor();
                retrieveSchedule(group, System.currentTimeMillis())
                        .subscribe(new ScheduleReceivedSubscriber(group));
                break;
            case LOADING_GROUPS:
                view.showProgress();
                break;
            case LOADING_GROUPS_FAILED:
                view.showCouldNotLoadGroupsErrorView();
                break;
            case LOADING_SCHEDULE:
                view.showProgress();
                break;
            case LOADING_SCHEDULE_FAILED:
                view.showCouldNotGetScheduleErrorView(preferences.getGroupToShowScheduleFor());
                break;
            case NO_GROUP_SELECTED:
                List<String> groupNames = repository.getGroupNames();
                if (groupNames == null) {
                    state = State.LOADING_GROUPS;
                    if (view != null) {
                        view.showProgress();
                    }
                    loadGroups().subscribe(new GroupsLoadedSubscriber());

                } else {
                    state = State.NO_GROUP_SELECTED;
                    if (view != null) {
                        view.showNoGroupSelectedView(groupNames);
                    }
                }
                break;
            case REFRESHING_SCHEDULE:
                view.setRefreshing(true);
                break;
        }
    }

    private static Calendar getTodayCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        return calendar;
    }
}
