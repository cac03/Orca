package com.caco3.orca;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.caco3.orca.credentials.CredentialsModule;
import com.caco3.orca.data.orioks.OrioksRepositoryModule;
import com.caco3.orca.data.schedule.ScheduleRepositoryModule;
import com.caco3.orca.disciplinedetails.DaggerDisciplineDetailsComponent;
import com.caco3.orca.disciplinedetails.DisciplineDetailsComponent;
import com.caco3.orca.entrypoint.DaggerEntryPointComponent;
import com.caco3.orca.entrypoint.EntryPointComponent;
import com.caco3.orca.header.DaggerHeaderComponent;
import com.caco3.orca.header.HeaderComponent;
import com.caco3.orca.header.HeaderModule;
import com.caco3.orca.learning.DaggerLearningComponent;
import com.caco3.orca.learning.LearningComponent;
import com.caco3.orca.learning.preferences.LearningPreferencesModule;
import com.caco3.orca.login.DaggerLoginComponent;
import com.caco3.orca.login.LoginComponent;
import com.caco3.orca.orioks.OrioksModule;
import com.caco3.orca.orioksautoupdate.DaggerOrioksAutoUpdateComponent;
import com.caco3.orca.orioksautoupdate.OrioksAutoUpdateComponent;
import com.caco3.orca.schedule.DaggerScheduleComponent;
import com.caco3.orca.schedule.ScheduleComponent;
import com.caco3.orca.schedule.ScheduleModule;
import com.caco3.orca.schedule.SchedulePreferencesModule;
import com.caco3.orca.scheduleapi.ScheduleApiModule;
import com.caco3.orca.settings.DaggerSettingsComponent;
import com.caco3.orca.settings.SettingsComponent;
import com.caco3.orca.settings.SettingsModule;

import timber.log.Timber;


public class OrcaApp extends Application {

    @NonNull // initialized in onCreate()
    private ApplicationComponent applicationComponent;

    private DisciplineDetailsComponent disciplineDetailsComponent;
    private EntryPointComponent entryPointComponent;
    private HeaderComponent headerComponent;
    private LearningComponent learningComponent;
    private LoginComponent loginComponent;
    private OrioksAutoUpdateComponent orioksAutoUpdateComponent;
    private ScheduleComponent scheduleComponent;
    private SettingsComponent settingsComponent;

    @Override
    public void onCreate(){
        Timber.plant(new Timber.DebugTree());
        applicationComponent = prepareApplicationComponent().build();
        initDaggerComponents();
    }
    /**
     * Static method returns {@link OrcaApp} instance from context
     * @param context to get {@link OrcaApp}
     * @return {@link OrcaApp} instance
     */
    public static OrcaApp get(Context context) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        return (OrcaApp) context.getApplicationContext();
    }

    /**
     * Integration test mocked Application will override it
     * to provide mocked dependencies
     * @return {@link com.caco3.orca.DaggerApplicationComponent.Builder} with all needed modules set
     */
    protected DaggerApplicationComponent.Builder prepareApplicationComponent(){
        return DaggerApplicationComponent.builder()
                .orioksModule(new OrioksModule())
                .credentialsModule(new CredentialsModule())
                .applicationModule(new ApplicationModule(this))
                .scheduleApiModule(new ScheduleApiModule())
                .scheduleRepositoryModule(new ScheduleRepositoryModule())
                .schedulePreferencesModule(new SchedulePreferencesModule())
                .orioksRepositoryModule(new OrioksRepositoryModule())
                .headerModule(new HeaderModule())
                .settingsModule(new SettingsModule())
                .learningPreferencesModule(new LearningPreferencesModule());
    }

    @NonNull
    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

    private void initDaggerComponents(){
        this.disciplineDetailsComponent = initDisciplineDetailsComponent();
        this.entryPointComponent = initEntryPointComponent();
        this.headerComponent = initHeaderComponent();
        this.learningComponent = initLearningComponent();
        this.loginComponent = initLoginComponent();
        this.orioksAutoUpdateComponent = initOrioksAutoUpdateComponent();
        this.scheduleComponent = initScheduleComponent();
        this.settingsComponent = initSettingsComponent();
    }

    private DisciplineDetailsComponent initDisciplineDetailsComponent(){
        return DaggerDisciplineDetailsComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    private EntryPointComponent initEntryPointComponent(){
        return DaggerEntryPointComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    private HeaderComponent initHeaderComponent(){
        return DaggerHeaderComponent
                .builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    private LearningComponent initLearningComponent(){
        return DaggerLearningComponent
                .builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    private LoginComponent initLoginComponent(){
        return DaggerLoginComponent
                .builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    private OrioksAutoUpdateComponent initOrioksAutoUpdateComponent(){
        return DaggerOrioksAutoUpdateComponent
                .builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    private ScheduleComponent initScheduleComponent(){
        return DaggerScheduleComponent
                .builder()
                .applicationComponent(applicationComponent)
                .scheduleModule(new ScheduleModule())
                .build();
    }

    private SettingsComponent initSettingsComponent(){
        return DaggerSettingsComponent
                .builder()
                .applicationComponent(applicationComponent)
                .settingsModule(new SettingsModule())
                .build();
    }

    public DisciplineDetailsComponent getDisciplineDetailsComponent() {
        return disciplineDetailsComponent;
    }

    public EntryPointComponent getEntryPointComponent() {
        return entryPointComponent;
    }

    public HeaderComponent getHeaderComponent() {
        return headerComponent;
    }

    public LearningComponent getLearningComponent() {
        return learningComponent;
    }

    public LoginComponent getLoginComponent() {
        return loginComponent;
    }

    public OrioksAutoUpdateComponent getOrioksAutoUpdateComponent() {
        return orioksAutoUpdateComponent;
    }

    public ScheduleComponent getScheduleComponent() {
        return scheduleComponent;
    }

    public SettingsComponent getSettingsComponent() {
        return settingsComponent;
    }
}
