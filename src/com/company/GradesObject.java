package com.company;

import com.sun.prism.Graphics;

public class GradesObject {
    private String course_names;
    private String course_grades;

    GradesObject(String course_names, String course_grades){
        this.course_grades = course_grades;
        this.course_names = course_names;
    }

    public String getCourse_names() {
        return course_names;
    }

    public void setCourse_names(String course_names) {
        this.course_names = course_names;
    }

    public String getCourse_grades() {
        return course_grades;
    }

    public void setCourse_grades(String course_grades) {
        this.course_grades = course_grades;
    }
}
