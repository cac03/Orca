package com.caco3.orca.scheduleapi;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ScheduleApiResponseInternalTest {
    private static final Gson gson = new Gson();

    private static final String VALID_JSON = "{\"Times\":[{\"Time\":\"1 пара\",\"Code\":1,\"TimeFrom\":\"0001-01-01T09:00:00\",\"TimeTo\":\"0001-01-01T10:30:00\"},{\"Time\":\"2 пара\",\"Code\":2,\"TimeFrom\":\"0001-01-01T10:40:00\",\"TimeTo\":\"0001-01-01T12:10:00\"},{\"Time\":\"3 пара \",\"Code\":3,\"TimeFrom\":\"0001-01-01T12:20:00\",\"TimeTo\":\"0001-01-01T13:50:00\"},{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},{\"Time\":\"6 пара\",\"Code\":7,\"TimeFrom\":\"0001-01-01T18:10:00\",\"TimeTo\":\"0001-01-01T19:40:00\"},{\"Time\":\"7 пара\",\"Code\":8,\"TimeFrom\":\"0001-01-01T19:50:00\",\"TimeTo\":\"0001-01-01T21:20:00\"}],\"Data\":[{\"Day\":3,\"DayNumber\":0,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0006671\",\"Name\":\"Философия [Пр]\",\"TeacherFull\":\"Кнэхт Наталья Петровна\",\"Teacher\":\"Кнэхт Н.П.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":5,\"Name\":\"1205 (м)\"}},{\"Day\":3,\"DayNumber\":0,\"Time\":{\"Time\":\"6 пара\",\"Code\":7,\"TimeFrom\":\"0001-01-01T18:10:00\",\"TimeTo\":\"0001-01-01T19:40:00\"},\"Class\":{\"Code\":\"0007280\",\"Name\":\"Философия [Лек]\",\"TeacherFull\":\"Кнэхт Наталья Петровна\",\"Teacher\":\"Кнэхт Н.П.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":5,\"Name\":\"1205 (м)\"}},{\"Day\":3,\"DayNumber\":0,\"Time\":{\"Time\":\"3 пара \",\"Code\":3,\"TimeFrom\":\"0001-01-01T12:20:00\",\"TimeTo\":\"0001-01-01T13:50:00\"},\"Class\":{\"Code\":\"0008946\",\"Name\":\"Математические методы для физиков и инженеров [Пр]\",\"TeacherFull\":\"Алфимов Георгий Леонидович\",\"Teacher\":\"Алфимов Г.Л.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":11,\"Name\":\"4340\"}},{\"Day\":3,\"DayNumber\":0,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0008946\",\"Name\":\"Математические методы для физиков и инженеров [Пр]\",\"TeacherFull\":\"Алфимов Георгий Леонидович\",\"Teacher\":\"Алфимов Г.Л.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":11,\"Name\":\"4340\"}},{\"Day\":3,\"DayNumber\":0,\"Time\":{\"Time\":\"1 пара\",\"Code\":1,\"TimeFrom\":\"0001-01-01T09:00:00\",\"TimeTo\":\"0001-01-01T10:30:00\"},\"Class\":{\"Code\":\"0009279\",\"Name\":\"Программирование графических ускорителей (GPU) [Пр]\",\"TeacherFull\":\"Ашарина Ирина Владимировна\",\"Teacher\":\"Ашарина И.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":87,\"Name\":\"4338 у\"}},{\"Day\":3,\"DayNumber\":0,\"Time\":{\"Time\":\"2 пара\",\"Code\":2,\"TimeFrom\":\"0001-01-01T10:40:00\",\"TimeTo\":\"0001-01-01T12:10:00\"},\"Class\":{\"Code\":\"0009279\",\"Name\":\"Программирование графических ускорителей (GPU) [Пр]\",\"TeacherFull\":\"Ашарина Ирина Владимировна\",\"Teacher\":\"Ашарина И.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":87,\"Name\":\"4338 у\"}},{\"Day\":3,\"DayNumber\":1,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0007468\",\"Name\":\"Математическая логика и лингвистика [Лек]\",\"TeacherFull\":\"Кожухов Игорь Борисович\",\"Teacher\":\"Кожухов И.Б.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":3,\"DayNumber\":1,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0007766\",\"Name\":\"Математическая логика и лингвистика [Пр]\",\"TeacherFull\":\"Кожухов Игорь Борисович\",\"Teacher\":\"Кожухов И.Б.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":3,\"DayNumber\":2,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0006671\",\"Name\":\"Философия [Пр]\",\"TeacherFull\":\"Кнэхт Наталья Петровна\",\"Teacher\":\"Кнэхт Н.П.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":5,\"Name\":\"1205 (м)\"}},{\"Day\":3,\"DayNumber\":2,\"Time\":{\"Time\":\"6 пара\",\"Code\":7,\"TimeFrom\":\"0001-01-01T18:10:00\",\"TimeTo\":\"0001-01-01T19:40:00\"},\"Class\":{\"Code\":\"0007280\",\"Name\":\"Философия [Лек]\",\"TeacherFull\":\"Кнэхт Наталья Петровна\",\"Teacher\":\"Кнэхт Н.П.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":5,\"Name\":\"1205 (м)\"}},{\"Day\":3,\"DayNumber\":2,\"Time\":{\"Time\":\"3 пара \",\"Code\":3,\"TimeFrom\":\"0001-01-01T12:20:00\",\"TimeTo\":\"0001-01-01T13:50:00\"},\"Class\":{\"Code\":\"0008946\",\"Name\":\"Математические методы для физиков и инженеров [Пр]\",\"TeacherFull\":\"Алфимов Георгий Леонидович\",\"Teacher\":\"Алфимов Г.Л.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":11,\"Name\":\"4340\"}},{\"Day\":3,\"DayNumber\":2,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0008946\",\"Name\":\"Математические методы для физиков и инженеров [Пр]\",\"TeacherFull\":\"Алфимов Георгий Леонидович\",\"Teacher\":\"Алфимов Г.Л.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":11,\"Name\":\"4340\"}},{\"Day\":3,\"DayNumber\":2,\"Time\":{\"Time\":\"1 пара\",\"Code\":1,\"TimeFrom\":\"0001-01-01T09:00:00\",\"TimeTo\":\"0001-01-01T10:30:00\"},\"Class\":{\"Code\":\"0009279\",\"Name\":\"Программирование графических ускорителей (GPU) [Пр]\",\"TeacherFull\":\"Ашарина Ирина Владимировна\",\"Teacher\":\"Ашарина И.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":87,\"Name\":\"4338 у\"}},{\"Day\":3,\"DayNumber\":2,\"Time\":{\"Time\":\"2 пара\",\"Code\":2,\"TimeFrom\":\"0001-01-01T10:40:00\",\"TimeTo\":\"0001-01-01T12:10:00\"},\"Class\":{\"Code\":\"0009279\",\"Name\":\"Программирование графических ускорителей (GPU) [Пр]\",\"TeacherFull\":\"Ашарина Ирина Владимировна\",\"Teacher\":\"Ашарина И.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":87,\"Name\":\"4338 у\"}},{\"Day\":3,\"DayNumber\":3,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0007468\",\"Name\":\"Математическая логика и лингвистика [Лек]\",\"TeacherFull\":\"Кожухов Игорь Борисович\",\"Teacher\":\"Кожухов И.Б.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":3,\"DayNumber\":3,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0007766\",\"Name\":\"Математическая логика и лингвистика [Пр]\",\"TeacherFull\":\"Кожухов Игорь Борисович\",\"Teacher\":\"Кожухов И.Б.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":5,\"DayNumber\":0,\"Time\":{\"Time\":\"6 пара\",\"Code\":7,\"TimeFrom\":\"0001-01-01T18:10:00\",\"TimeTo\":\"0001-01-01T19:40:00\"},\"Class\":{\"Code\":\"0008571\",\"Name\":\"Распознавание образов и машинное обучение [Лаб]\",\"TeacherFull\":\"Малистов Алексей Сергеевич\",\"Teacher\":\"Малистов А.С.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":6,\"Name\":\"3102 (м)\"}},{\"Day\":5,\"DayNumber\":0,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0008988\",\"Name\":\"Распознавание образов и машинное обучение [Пр]\",\"TeacherFull\":\"Малистов Алексей Сергеевич\",\"Teacher\":\"Малистов А.С.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":6,\"Name\":\"3102 (м)\"}},{\"Day\":5,\"DayNumber\":1,\"Time\":{\"Time\":\"6 пара\",\"Code\":7,\"TimeFrom\":\"0001-01-01T18:10:00\",\"TimeTo\":\"0001-01-01T19:40:00\"},\"Class\":{\"Code\":\"0008571\",\"Name\":\"Распознавание образов и машинное обучение [Лаб]\",\"TeacherFull\":\"Малистов Алексей Сергеевич\",\"Teacher\":\"Малистов А.С.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":6,\"Name\":\"3102 (м)\"}},{\"Day\":5,\"DayNumber\":1,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0009302\",\"Name\":\"Принципы построения математических моделей [Пр]\",\"TeacherFull\":\"Гончаров Виктор Анатольевич\",\"Teacher\":\"Гончаров В.А.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":5,\"DayNumber\":1,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0009302\",\"Name\":\"Принципы построения математических моделей [Пр]\",\"TeacherFull\":\"Гончаров Виктор Анатольевич\",\"Teacher\":\"Гончаров В.А.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":5,\"DayNumber\":2,\"Time\":{\"Time\":\"6 пара\",\"Code\":7,\"TimeFrom\":\"0001-01-01T18:10:00\",\"TimeTo\":\"0001-01-01T19:40:00\"},\"Class\":{\"Code\":\"0008571\",\"Name\":\"Распознавание образов и машинное обучение [Лаб]\",\"TeacherFull\":\"Малистов Алексей Сергеевич\",\"Teacher\":\"Малистов А.С.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":6,\"Name\":\"3102 (м)\"}},{\"Day\":5,\"DayNumber\":2,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0008988\",\"Name\":\"Распознавание образов и машинное обучение [Пр]\",\"TeacherFull\":\"Малистов Алексей Сергеевич\",\"Teacher\":\"Малистов А.С.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":6,\"Name\":\"3102 (м)\"}},{\"Day\":5,\"DayNumber\":3,\"Time\":{\"Time\":\"6 пара\",\"Code\":7,\"TimeFrom\":\"0001-01-01T18:10:00\",\"TimeTo\":\"0001-01-01T19:40:00\"},\"Class\":{\"Code\":\"0008571\",\"Name\":\"Распознавание образов и машинное обучение [Лаб]\",\"TeacherFull\":\"Малистов Алексей Сергеевич\",\"Teacher\":\"Малистов А.С.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":6,\"Name\":\"3102 (м)\"}},{\"Day\":5,\"DayNumber\":3,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0009302\",\"Name\":\"Принципы построения математических моделей [Пр]\",\"TeacherFull\":\"Гончаров Виктор Анатольевич\",\"Teacher\":\"Гончаров В.А.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":5,\"DayNumber\":3,\"Time\":{\"Time\":\"5 пара\",\"Code\":6,\"TimeFrom\":\"0001-01-01T16:00:00\",\"TimeTo\":\"0001-01-01T17:30:00\"},\"Class\":{\"Code\":\"0009302\",\"Name\":\"Принципы построения математических моделей [Пр]\",\"TeacherFull\":\"Гончаров Виктор Анатольевич\",\"Teacher\":\"Гончаров В.А.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":63,\"Name\":\"4306\"}},{\"Day\":6,\"DayNumber\":1,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0008277\",\"Name\":\"Компьютерное зрение [Лаб]\",\"TeacherFull\":\"Голованов Роман Вячеславович\",\"Teacher\":\"Голованов Р.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":41,\"Name\":\"3109 (м)\"}},{\"Day\":6,\"DayNumber\":1,\"Time\":{\"Time\":\"3 пара \",\"Code\":3,\"TimeFrom\":\"0001-01-01T12:20:00\",\"TimeTo\":\"0001-01-01T13:50:00\"},\"Class\":{\"Code\":\"0009582\",\"Name\":\"Компьютерное зрение [Пр]\",\"TeacherFull\":\"Голованов Роман Вячеславович\",\"Teacher\":\"Голованов Р.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":41,\"Name\":\"3109 (м)\"}},{\"Day\":6,\"DayNumber\":3,\"Time\":{\"Time\":\"4 пара\",\"Code\":5,\"TimeFrom\":\"0001-01-01T14:20:00\",\"TimeTo\":\"0001-01-01T15:50:00\"},\"Class\":{\"Code\":\"0008277\",\"Name\":\"Компьютерное зрение [Лаб]\",\"TeacherFull\":\"Голованов Роман Вячеславович\",\"Teacher\":\"Голованов Р.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":41,\"Name\":\"3109 (м)\"}},{\"Day\":6,\"DayNumber\":3,\"Time\":{\"Time\":\"3 пара \",\"Code\":3,\"TimeFrom\":\"0001-01-01T12:20:00\",\"TimeTo\":\"0001-01-01T13:50:00\"},\"Class\":{\"Code\":\"0009582\",\"Name\":\"Компьютерное зрение [Пр]\",\"TeacherFull\":\"Голованов Роман Вячеславович\",\"Teacher\":\"Голованов Р.В.\"},\"Group\":{\"Code\":\"ВМ-21\",\"Name\":\"ВМ-21\"},\"Room\":{\"Code\":41,\"Name\":\"3109 (м)\"}}],\"Semestr\":\"Осенний семестр 2016-2017 г. \"}";

    private static ScheduleApiResponseInternal response;

    @BeforeClass
    public static void parseResponse(){
        response = gson.fromJson(VALID_JSON, ScheduleApiResponseInternal.class);
    }

    @Test
    public void thereIs30DataObjects(){
        assertEquals(30, response.scheduleItems.length);
    }

    @Test
    public void semesterStringIsCorrect(){
        assertEquals("Осенний семестр 2016-2017 г. ", response.semesterDescription);
    }

    @Test
    public void thereIsComputerVisionLessonAtSaturday(){
        boolean found = false;
        for(ScheduleItemInternal scheduleItem : response.scheduleItems) {
            if (scheduleItem.classInternal.name.equals("Компьютерное зрение [Пр]")){
                found = true;
                break;
            }
        }

        assertTrue(found);
    }
}
