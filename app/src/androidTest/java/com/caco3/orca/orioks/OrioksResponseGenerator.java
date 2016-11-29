package com.caco3.orca.orioks;

import com.caco3.orca.orioks.model.ControlEvent;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.Group;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.orioks.model.Student;
import com.caco3.orca.orioks.model.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Util class helps to generate mock {@link OrioksResponse}
 */
public class OrioksResponseGenerator {
    private static final String[] names = {
            "Наталья", "Ульяна", "Яна", "Ирина", "Соня"
    };

    private static final String[] lastNames = {
            "Иванова", "Пирогова", "Циммерманн"
    };

    private static final String[] patronimycs = {
            "Владимировна", null, "Ивановна", "Руслановка"
    };

    private static final String[] groupNames = {
            "МП-843", "Д-666", "Ин-517"
    };

    private static String[] controlEventTypes = {
            "Лабораторная работа", "Экзамен",  "Зачет", "Контрольная работа"
    };

    private static String[] controlEventsTopics = {
            "Arrays", "Effective", "Coffee", "Green tea", null, null, null, null, null, null
    };

    private static String[] disciplineNames = {
            "Мемология", "Физика", "Оранжевание", "Микропроцессоры и микропроцессоры",
            "Хохотология"
    };

    private static String[] assessmentTypes = {
            "Бутылка", "Зачет", "Экзамен", "Халява", "Дифференцированный зачет"
    };

    private static final String[] departments = {
            "Иповс", "Мемологии", "Матана", "Петросянства", "Теоретического фантазарования"
    };



    private static final Random random = new Random();

    private static ControlEvent generateControlEvent(){
        return ControlEvent.builder()
                .achievedPoints((int)(random.nextFloat() * 10))
                .maxAvailablePoints(10)
                .at(random.nextInt())
                .entered(random.nextBoolean())
                .enteredBy(generateTeacher())
                .hasSeen(random.nextBoolean())
                .typeName(controlEventTypes[random.nextInt(controlEventTypes.length)])
                .topic(controlEventsTopics[random.nextInt(controlEventsTopics.length)])
                .build();
    }

    public static OrioksResponse generateOrioksResponse(){
        return new OrioksResponse() {
            @Override
            public List<Discipline> getDisciplines() {
                return generateDisciplines(12);
            }

            @Override
            public Student getStudent() {
                return generateStudent();
            }

            @Override
            public int getCurrentSemester() {
                return random.nextInt();
            }

            @Override
            public int getCurrentWeek() {
                return random.nextInt();
            }
        };
    }

    private static List<ControlEvent> generateControlEvents(int n) {
        List<ControlEvent> res = new ArrayList<>();
        while (--n > 0) {
            res.add(generateControlEvent());
        }

        return res;
    }

    private static List<Teacher> generateTeachers(int n) {
        List<Teacher> res = new ArrayList<>();
        while (--n > 0) {
            res.add(generateTeacher());
        }

        return res;
    }


    private static List<Discipline> generateDisciplines(int n){
        List<Discipline> res = new ArrayList<>();
        while (--n > 0) {
            res.add(generateDiscipline());
        }

        return res;
    }

    private static Discipline generateDiscipline(){
        return new Discipline.Builder()
                .controlEvents(generateControlEvents(15))
                .teacher(generateTeachers(5))
                .assessmentType(assessmentTypes[random.nextInt(assessmentTypes.length)])
                .department(departments[random.nextInt(departments.length)])
                .name(disciplineNames[random.nextInt(disciplineNames.length)])
                .build();
    }

    private static Teacher generateTeacher(){
        return new Teacher(names[random.nextInt(names.length)],
                lastNames[random.nextInt(lastNames.length)],
                patronimycs[random.nextInt(patronimycs.length)]);
    }

    private static Student generateStudent(){
        return new Student(generateGroup(),
                names[random.nextInt(names.length)],
                lastNames[random.nextInt(lastNames.length)],
                patronimycs[random.nextInt(patronimycs.length)]);
    }

    private static Group generateGroup(){
        return new Group(groupNames[random.nextInt(groupNames.length)]);
    }
}
