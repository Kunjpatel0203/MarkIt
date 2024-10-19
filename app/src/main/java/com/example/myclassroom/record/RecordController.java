package com.example.myclassroom.record;

import java.util.ArrayList;

class RecordController {
    final ArrayList<Record> records = new ArrayList<>();
    private final RecordService service;

    RecordController(RecordService service) {
        this.service = service;
        records.addAll(service.getAllRecords());
    }
}
