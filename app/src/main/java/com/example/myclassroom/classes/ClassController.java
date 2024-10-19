package com.example.myclassroom.classes;

import java.util.ArrayList;

class ClassController {
    final ArrayList<ClassItem> classItems = new ArrayList<>();
    private final ClassService service;

    ClassController(ClassService service) {
        this.service = service;
        classItems.addAll(service.getAllClasses());
    }

    public void addClass(ClassItem classItem) {
        long id = service.addClass(classItem);
        if (id == -1) return;
        classItem.setId(id);
        classItems.add(classItem);
    }

    public void updateClass(int position,ClassItem classItem) {
        int res = service.updateClass(classItem);
        if (res == -1) return;
        classItems.set(position, classItem);
    }
    public void deleteClass(int position) {
        int res = service.deleteClass(classItems.get(position));
        if (res == -1) return;
        classItems.remove(position);
    }

    public void refreshClasses() {
        classItems.clear();
        classItems.addAll(service.getAllClasses());
    }
}
