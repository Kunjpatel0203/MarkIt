package com.example.myclassroom.student;

import com.example.myclassroom.Constant;

import java.util.ArrayList;
import java.util.Map;

class StudentController {
    private final ArrayList<Student> students = new ArrayList<>();
    private Map<Long, String> status; //id:status
    private String attendanceDate = Constant.getToday();
    private final StudentService service;

    public void updateAttendance(String attendanceDate) {
        this.attendanceDate = attendanceDate;
        status.clear();
        Map<Long, String> newStatus = service.getStatus(attendanceDate);
        for (long i : newStatus.keySet()) {
            status.put(i, newStatus.get(i));
        }
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Map<Long, String> getStatus() {
        return status;
    }

    StudentController(StudentService service) {
        this.service = service;
        students.addAll(service.getAllStudents());
        status = service.getStatus(attendanceDate);
        students.sort((o1, o2)-> {
            if(o1.getRoll().length()!=o2.getRoll().length())return o1.getRoll().length()-o2.getRoll().length();
            return o1.getRoll().compareTo(o2.getRoll());
        });
    }

    public void addStudent(Student student) throws Exception {
        long id = service.addStudent(student);
        if (id == -1) throw new Exception("Already exists");
        student.setId(id);
        students.add(student);
        students.sort((o1, o2)-> {
            if(o1.getRoll().length()!=o2.getRoll().length())return o1.getRoll().length()-o2.getRoll().length();
            return o1.getRoll().compareTo(o2.getRoll());
        });
    }

    public void updateStudent(int position, Student student) {
        int res = service.updateStudent(student);
        if (res == -1) throw new NumberFormatException();
        students.set(position, student);
        students.sort((o1, o2)-> {
            if(o1.getRoll().length()!=o2.getRoll().length())return o1.getRoll().length()-o2.getRoll().length();
            return o1.getRoll().compareTo(o2.getRoll());
        });
    }

    public void deleteStudent(int position) {
        int res = service.deleteStudent(students.get(position));
        if (res == -1) return;
        students.remove(position);

    }

    public void saveStatusData() {
        if (status.size() != students.size()) {
            for (Student student : students) {
                if (!status.containsKey(student.getId()))
                    status.put(student.getId(), Constant.ABSENT);
            }
        }
        service.saveStatusData(status, attendanceDate);
    }
}
