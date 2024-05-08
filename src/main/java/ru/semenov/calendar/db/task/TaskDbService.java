package ru.semenov.calendar.db.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskDbService {

    private final TaskRepository repository;

    public Task getTask(long taskId) {
        return repository.findById(taskId).orElseThrow(NoSuchElementException::new);
    }

    public Task saveTask(Task task) {
        return repository.save(task);
    }

    public List<Task> getFiveApplicationsForClient(String employeeId, Integer offset, String sortBy) {
        return new ArrayList<>();
    }

    public List<Task> getFiveSentApplications(Integer offset, String sortBy, String s) {

        return new ArrayList<>();
    }

    public List<Task> getFiveApplications(Integer offset, String sortBy, String s) {
        return new ArrayList<>();
    }
}
