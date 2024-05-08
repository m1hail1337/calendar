package ru.semenov.calendar.service.task;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semenov.calendar.db.employee.Employee;
import ru.semenov.calendar.db.employee.Role;
import ru.semenov.calendar.db.task.Task;
import ru.semenov.calendar.db.task.TaskDbService;
import ru.semenov.calendar.db.task.TaskStatus;
import ru.semenov.calendar.dto.calendar.DayEvent;
import ru.semenov.calendar.dto.task.TaskDto;
import ru.semenov.calendar.dto.task.creation.CreationRequest;
import ru.semenov.calendar.dto.task.edit.EditRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private static final String EMPTY_NAME = "";
    private final TaskDbService taskDbService;

    public TaskDto getTask(long taskId) {
        Task requestedTask = taskDbService.getTask(taskId);
        return convertToDto(requestedTask);
    }

    public String createTask(CreationRequest request, Employee employee) {
        Task taskToCreate = Task.builder()
            .employee(employee)
            .message(request.message())
            .creationDate(LocalDateTime.parse(request.creationTime()))
            .build();
        Task createdTask = taskDbService.saveTask(taskToCreate);
        return String.valueOf(createdTask.getId());
    }

    public void editTask(EditRequest request, Employee employee, long taskId) {
        Task task = taskDbService.getTask(taskId);

        if (!isTaskBelongsToEmployee(employee.getId(), task) && !employee.getRoles().contains(Role.ROLE_MANAGER)) {
            throw new AccessDeniedException("У пользователя нет прав на изменение задачи");
        }

        task.setMessage(request.message());
        taskDbService.saveTask(task);
    }

    public List<TaskDto> getEmployeeTasks(Employee employee, Integer offset, String sortBy) {
        String employeeId = employee.getId();
        List<Task> page = taskDbService.getFiveApplicationsForClient(employeeId, offset, sortBy);
        return page.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * @param firstName может быть не целым именем пользователя, а частью. Например, "Iva" для имени "Ivan".
     */
    public List<TaskDto> getSentTasks(Integer offset, String sortBy, String firstName) {
        List<Task> page = taskDbService.getFiveSentApplications(
            offset,
            sortBy,
            firstName != null ? firstName : EMPTY_NAME
        );
        return page.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * @param firstName может быть не целым именем пользователя, а частью. Например, "Iva" для имени "Ivan".
     */
    public List<TaskDto> getAllTasks(Integer offset, String sortBy, String firstName) {
        List<Task> page = taskDbService.getFiveApplications(
            offset,
            sortBy,
            firstName != null ? firstName : EMPTY_NAME
        );
        return page.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public void open(Employee employee, long taskId) {
        Task task = taskDbService.getTask(taskId);

        if (!isTaskBelongsToEmployee(employee.getId(), task)) {
            throw new RuntimeException("Клиент пытается изменить чужую заявку.");
        }

        if (task.getStatus() != TaskStatus.SKETCH) {
            throw new RuntimeException("Клиент пытается изменить отправленную заявку");
        }

        task.setStatus(TaskStatus.OPEN);
        taskDbService.saveTask(task);
    }

    public TaskDto getSentTask(long taskId) {
        Task task = taskDbService.getTask(taskId);
        if (task.getStatus() == TaskStatus.OPEN) {
            return convertToDto(task);
        }

        throw new RuntimeException("Заявка не находится в статусе OPEN");
    }

    @Transactional
    public void changeStatus(long taskId, TaskStatus status) {
        Task task = taskDbService.getTask(taskId);

        if (task.getStatus() != TaskStatus.OPEN) {
            throw new RuntimeException("Заявка не находится в статусе OPEN");
        }
        if (status == TaskStatus.ACCEPTED || status == TaskStatus.DECLINED) {
            task.setStatus(status);
            taskDbService.saveTask(task);
            return;
        }

        throw new RuntimeException("Оператор не может установить статус задачи: " + status.name());
    }

    private TaskDto convertToDto(Task task) {
        return TaskDto.builder()
            .id(task.getId())
            .creationDate(task.getCreationDate())
            .status(task.getStatus())
            .message(task.getMessage())
            .firstName(task.getEmployee().getFirstName())
            .lastName(task.getEmployee().getLastName())
            .phoneNumber(task.getEmployee().getPhoneNumber())
            .build();
    }

    private boolean isTaskBelongsToEmployee(String employeeId, Task task) {
        return employeeId.equals(task.getEmployee().getId());
    }

    public List<DayEvent> getTeamTasks(String teamName, Employee employee, Integer offset, String sortBy) {
        // TODO
        return null;
    }

    public void deleteTask(final Employee employee, final long taskId) {
        // TODO
    }
}
