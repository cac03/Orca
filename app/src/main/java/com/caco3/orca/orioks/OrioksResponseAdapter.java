package com.caco3.orca.orioks;

import com.caco3.orca.orioks.model.ControlEvent;
import com.caco3.orca.orioks.model.Discipline;
import com.caco3.orca.orioks.model.Group;
import com.caco3.orca.orioks.model.OrioksResponse;
import com.caco3.orca.orioks.model.Student;
import com.caco3.orca.orioks.model.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Util class. Converts internal raw {@link OrioksResponseJson} to {@link OrioksResponse}
 */
/*package*/ class OrioksResponseAdapter {

    /*package*/ static OrioksResponse convert(OrioksResponseJson orioksResponseJson) {
        Student student = getStudent(orioksResponseJson);
        List<Discipline> disciplines = new ArrayList<>();
        for(DisciplineJson disciplineJson : orioksResponseJson.disciplines.values()) {
            disciplines.add(convertDiscipline(disciplineJson, orioksResponseJson.departments));
        }
        return new OrioksResponse(disciplines,
                student,
                orioksResponseJson.currentSemester,
                orioksResponseJson.currentWeek);
    }

    private static Student getStudent(OrioksResponseJson orioksResponseJson) {
        return new Student(new Group(orioksResponseJson.group.name), orioksResponseJson.firstName, orioksResponseJson.lastName, orioksResponseJson.patronymic);
    }
    private static ControlEvent convertControlEventJson(ControlEventJson controlEventJson) {
        return ControlEvent.builder()
                .typeName(controlEventJson.typeName)
                .enteredBy(new Teacher(controlEventJson.markJson.enteredBy))
                .topic(controlEventJson.subject)
                .at(controlEventJson.week)
                .bonus(!(controlEventJson.bonus instanceof String))
                .maxAvailablePoints(controlEventJson.maxPoints)
                .achievedPoints(controlEventJson.markJson.achievedPoints)
                .minPoints(controlEventJson.minPoints)
                .entered(Float.compare(controlEventJson.markJson.achievedPoints, 0.0f) == 0 && controlEventJson.markJson.enteredBy.contains("методи"))
                .build();
    }

    private static List<Teacher> convertTeachers(Map<Integer, TeacherJson> teacherJsonMap) {
        List<Teacher> teachers = new ArrayList<>();
        for(TeacherJson teacherJson : teacherJsonMap.values()) {
            teachers.add(new Teacher(teacherJson.name));
        }

        return teachers;
    }

    private static Discipline convertDiscipline(DisciplineJson disciplineJson, Map<Integer, DepartmentJson> departmentJsonMap) {
        List<ControlEvent> controlEvents = new ArrayList<>();
        for(ControlEventJson controlEventJson : disciplineJson.controlEvents) {
            controlEvents.add(convertControlEventJson(controlEventJson));
        }

        return new Discipline.Builder()
                .controlEvents(controlEvents)
                .teacher(convertTeachers(disciplineJson.teacherMap))
                .assessmentType(disciplineJson.assessmentType)
                .name(disciplineJson.name)
                .department(departmentJsonMap.get(disciplineJson.disciplineId).name)
                .build();
    }
}
