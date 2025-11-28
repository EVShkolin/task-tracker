package ru.kpfu.tasktracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.comment.CommentDto;
import ru.kpfu.tasktracker.dto.task.TaskDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.exception.ObjectNotFoundException;
import ru.kpfu.tasktracker.exception.TaskAssignmentException;
import ru.kpfu.tasktracker.mapper.TaskMapper;
import ru.kpfu.tasktracker.model.Task;
import ru.kpfu.tasktracker.repository.TaskRepository;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectMemberService projectMemberService;
    private final CommentService commentService;
    private final TaskMapper taskMapper;

    public TaskDto findById(Long id) {
        log.debug("IN TaskService find task by id {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("task", id));
        List<UserProfileDto> assignees = projectMemberService.findAllByTaskId(task.getId());
        List<CommentDto> comments = commentService.getAllCommentsByTaskId(id);
        TaskDto dto = taskMapper.toDto(task);
        dto.setAssignees(assignees);
        dto.setComments(comments);
        return dto;
    }

    public void addAssignee(Long taskId, Long memberId) {
        log.debug("IN TaskService addAssignee task {} to member {}", taskId, memberId);
        if (taskRepository.taskAndMemberInSameProject(taskId, memberId)) {
            taskRepository.addAssignee(taskId, memberId);
        } else {
            log.warn("Task {} already assigned to member {}", taskId, memberId);
            throw new TaskAssignmentException(taskId, memberId);
        }
    }

    public TaskDto save(TaskDto taskDto) {
        log.debug("IN TaskService save task {}", taskDto);
        Task task = taskMapper.fromDto(taskDto);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    public TaskDto update(Long id, TaskDto taskDto) {
        log.debug("IN TaskService update task {}", taskDto);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("task", id));
        task.setTitle(taskDto.getTitle());
        task.setContent(taskDto.getContent());
        task.setUpdatedAt(Instant.now());
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    public void updateStatus(Long taskId, Long cardId) {
        log.debug("IN TaskService update status task {} to card {}", taskId, cardId);
        if (taskRepository.taskAndCardInSameProject(taskId, cardId)) {
            taskRepository.updateStatus(taskId, cardId);
        } else {
            throw new TaskAssignmentException(taskId, cardId);
        }
    }

    public void delete(Long id) {
        log.debug("IN TaskService delete task {}", id);
        taskRepository.delete(id);
    }

    public void removeAssignee(Long taskId, Long memberId) {
        log.debug("IN TaskService remove assignee task {} member {}", taskId, memberId);
        taskRepository.removeAssignee(taskId, memberId);
    }
}
