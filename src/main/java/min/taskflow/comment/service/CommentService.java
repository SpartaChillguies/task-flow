package min.taskflow.comment.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.request.CommentUpdateRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.exception.CommentErrorCode;
import min.taskflow.comment.exception.CommentException;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.task.entity.Task;
import min.taskflow.task.service.queryService.InternalQueryTaskService;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final InternalQueryTaskService taskService;
    private final InternalQueryUserService userService;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse createComment(Long taskId,
                                         @Valid CommentRequest request,
                                         Long userId) {

        Task task = taskService.findByTaskId(taskId);

        if (request.parentId() != null) {
            Comment parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));

            if (!parentComment.getTask().getTaskId().equals(task.getTaskId())) {
                throw new CommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
            }
        }

        Comment comment = commentMapper.toEntity(request, task, userService.findByUserId(userId));
        Comment savedComment = commentRepository.save(comment);
        UserResponse userResponse = userService.toUserResponse(savedComment.getUser());

        return commentMapper.toCommentResponse(savedComment, userResponse);
    }

    @Transactional
    public Page<CommentResponse> getComments(Long taskId, Pageable pageable, String sort) {

        taskService.findByTaskId(taskId);

        Sort.Direction orderBy = sort.equals("oldest") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortByCreatedAt = Sort.by(orderBy, "createdAt");
        Pageable parentPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByCreatedAt);

        // 1. 부모 댓글 페이징 조회
        Page<Comment> parentComments = commentRepository.findByTask_TaskIdAndParentIdIsNull(taskId, parentPageable);
        List<CommentResponse> allComments = new ArrayList<>();

        for (Comment parentComment : parentComments.getContent()) {
            UserResponse parentUserResponse = userService.toUserResponse(parentComment.getUser());
            CommentResponse parentResponse = commentMapper.toCommentResponse(parentComment, parentUserResponse);

            // 2. 대댓글 조회
            List<Comment> childComments = commentRepository.findByParentId(parentComment.getCommentId(), sortByCreatedAt);
            List<CommentResponse> replies = childComments.stream()
                    .map(childComment -> {
                        UserResponse childUserResponse = userService.toUserResponse(childComment.getUser());
                        return commentMapper.toCommentResponse(childComment, childUserResponse);
                    })
                    .toList();

            allComments.add(parentResponse);
            allComments.addAll(replies);
        }

        PageImpl<CommentResponse> comments = new PageImpl<>(allComments, pageable, parentComments.getTotalElements());

        return comments;
    }

    @Transactional
    public CommentResponse updateComment(Long taskId, CommentUpdateRequest request, Long commentId, Long userId) {

        Comment comment = validateCommentAccess(taskId, commentId, userId);

        comment.updateContent(request.content());
        UserResponse userResponse = userService.toUserResponse(comment.getUser());

        return commentMapper.toCommentResponse(comment, userResponse);
    }

    @Transactional
    public int deleteComment(Long taskId, Long commentId, Long userId) {

        Comment comment = validateCommentAccess(taskId, commentId, userId);

        comment.delete();
        int deleteCount = 1;

        List<Comment> childComments = commentRepository.findByParentId(comment.getCommentId(), Sort.unsorted());

        for (Comment childComment : childComments) {
            childComment.delete();
            deleteCount++;
        }

        return deleteCount;
    }

    // 댓글이 해당 작업에 속하고 작성자가 요청한 사용자와 일치하는지 확인합니다.
    private Comment validateCommentAccess(Long taskId, Long commentId, Long userId) {

        taskService.findByTaskId(taskId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (comment.isDeleted()) {
            throw new CommentException(CommentErrorCode.COMMENT_ALREADY_DELETED);
        }

        if (!comment.getTask().getTaskId().equals(taskId)) {
            throw new CommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
        }

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new CommentException(CommentErrorCode.COMMENT_FORBIDDEN);
        }

        return comment;
    }
}
