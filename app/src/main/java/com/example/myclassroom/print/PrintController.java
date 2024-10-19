package com.example.myclassroom.print;

import com.example.myclassroom.classes.ClassItem;
import com.example.myclassroom.student.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class PrintController {
    final ClassItem classItem;
    final ArrayList<Student> students = new ArrayList<>();
    private final Map<Long,Map<Integer,String>> status;

    PrintController(PrintService service) {
        classItem = service.getClassItem();
        students.addAll(service.getAllStudents());
        students.sort((o1, o2)-> {
            if(o1.getRoll().length()!=o2.getRoll().length())return o1.getRoll().length()-o2.getRoll().length();
            return o1.getRoll().compareTo(o2.getRoll());
        });
        status = service.getMonthStatus();
    }

    public Map<Integer,String> getStudentStatus(long id) {
        return status.get(id);
    }
}
