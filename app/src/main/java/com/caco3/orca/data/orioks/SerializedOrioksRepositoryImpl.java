package com.caco3.orca.data.orioks;

import android.content.Context;

import com.caco3.orca.orioks.UserCredentials;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.orioks.model.Student;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Concrete {@link OrioksRepository} implementation where all operations are done
 * via writing/reading Serializable objects.
 *
 * Don't look code here it's awful and later {@link OrioksRepository} will be changed to another one
 */
/*package*/ class SerializedOrioksRepositoryImpl implements OrioksRepository {
    private final Context context;

    @Inject
    /*package*/ SerializedOrioksRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public void saveStudent(UserCredentials credentials, Student student) {
        saveToFile(student, credentials.getLogin() + "_student");
    }

    @Override
    public Student getStudent(UserCredentials userCredentials) {
        return readFromFile(Student.class, userCredentials.getLogin() + "_student");
    }

    @Override
    public void saveDisciplines(UserCredentials credentials,
                                Collection<Discipline> disciplines,
                                int semester) {
        saveToFile(disciplines, semester + "_" + credentials.getLogin() + "_disc");
    }

    @Override
    public List<Discipline> getDisciplines(UserCredentials credentials, int semester) {
        Object disciplines = readFromFile(List.class, semester + "_" + credentials.getLogin() + "_disc");
        if (disciplines == null) {
            return null;
        } else if (disciplines instanceof List){
            return (List<Discipline>)disciplines;
        } else {
            return new ArrayList<>((Collection<Discipline>)disciplines);
        }
    }

    private <S> S readFromFile(Class<S> sClass, String filename) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(context.openFileInput(filename));
            return (S) ois.readObject();
        } catch (FileNotFoundException e) {
            Timber.w(e);
            return null;
        } catch (IOException e) {
            Timber.e(e);
            throw new AssertionError(e);
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void saveToFile(Object object, String filename) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
            oos.writeObject(object);
        } catch (IOException e) {
            Timber.e(e);
            throw new AssertionError(e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }
    }

    @Override
    public void setCurrentSemester(UserCredentials credentials, int semester) {
        context.getSharedPreferences("orioks", Context.MODE_PRIVATE)
                .edit()
                .putInt(credentials.getLogin(), semester)
                .apply();
    }

    @Override
    public int getCurrentSemester(UserCredentials userCredentials) {
        return context.getSharedPreferences("orioks", Context.MODE_PRIVATE)
                .getInt(userCredentials.getLogin(), -1);
    }
}
