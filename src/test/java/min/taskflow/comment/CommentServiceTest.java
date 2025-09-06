package min.taskflow.comment;

import min.taskflow.comment.dto.request.CommentRequest;
import min.taskflow.comment.dto.request.CommentUpdateRequest;
import min.taskflow.comment.dto.response.CommentResponse;
import min.taskflow.comment.entity.Comment;
import min.taskflow.comment.exception.CommentException;
import min.taskflow.comment.mapper.CommentMapper;
import min.taskflow.comment.repository.CommentRepository;
import min.taskflow.comment.service.CommentService;
import min.taskflow.fixture.CommentFixture;
import min.taskflow.fixture.TaskFixture;
import min.taskflow.fixture.TeamFixture;
import min.taskflow.fixture.UserFixture;
import min.taskflow.task.entity.Task;
import min.taskflow.task.service.queryService.InternalQueryTaskService;
import min.taskflow.team.entity.Team;
import min.taskflow.user.dto.response.UserResponse;
import min.taskflow.user.entity.User;
import min.taskflow.user.service.queryService.InternalQueryUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private InternalQueryTaskService taskService;

    @Mock
    private InternalQueryUserService userService;

    @Test
    void createComment_댓글_생성_성공() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        CommentRequest request = new CommentRequest("댓글 내용", null);

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTask(user);
        Comment comment = CommentFixture.createComment(request.content(), task, user);

        UserResponse userResponse = UserFixture.createUserResponse(user, userId);
        CommentResponse commentResponse = CommentFixture
                .createCommentResponse(100L, "댓글 내용", taskId, userId, userResponse);

        when(taskService.findByTaskId(taskId)).thenReturn(task);
        when(userService.findByUserId(userId)).thenReturn(user);
        when(commentMapper.toEntity(request, task, user)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(userService.toUserResponse(user)).thenReturn(userResponse);
        when(commentMapper.toCommentResponse(any(Comment.class), eq(userResponse))).thenReturn(commentResponse);

        // when
        CommentResponse response = commentService.createComment(taskId, request, userId);

        // then
        assertNotNull(response);
        assertEquals("댓글 내용", response.content());
        assertEquals("김철수", response.user().name());
    }

    @Test
    void createComment_부모_댓글이_존재하지_않는_경우() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long parentId = 3L;
        CommentRequest request = new CommentRequest("댓글 내용", parentId);

        when(commentRepository.findById(parentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommentException.class, () -> commentService.createComment(taskId, request, userId));
    }

    @Test
    void createComment_부모_댓글이_존재하지만_다른_작업에_속한_경우() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long parentId = 3L;
        CommentRequest request = new CommentRequest("대댓글 내용", parentId);

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTask(user);
        Task otherTask = TaskFixture.createTaskWithId(user, 20L);
        Comment parentComment = CommentFixture.createComment(request.content(), otherTask, user);

        when(taskService.findByTaskId(taskId)).thenReturn(task);
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parentComment));

        // when & then
        assertThrows(CommentException.class, () -> commentService.createComment(taskId, request, userId));
    }

    @Test
    void createComment_대댓글_작성_성공() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long parentId = 3L;
        CommentRequest request = new CommentRequest("대댓글 내용", parentId);

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTaskWithId(user, 20L);
        Comment parentComment = CommentFixture.createComment("부모 댓글", task, user);
        Comment childComment = CommentFixture.createComment(request.content(), task, user);

        UserResponse userResponse = UserFixture.createUserResponse(user, userId);
        CommentResponse commentResponse = CommentFixture
                .createCommentResponse(200L, "대댓글 내용", taskId, userId, userResponse);

        when(taskService.findByTaskId(taskId)).thenReturn(task);
        when(userService.findByUserId(userId)).thenReturn(user);
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parentComment));
        when(commentMapper.toEntity(request, task, user)).thenReturn(childComment);
        when(commentRepository.save(any(Comment.class))).thenReturn(childComment);
        when(userService.toUserResponse(user)).thenReturn(userResponse);
        when(commentMapper.toCommentResponse(any(Comment.class), eq(userResponse))).thenReturn(commentResponse);

        // when
        CommentResponse response = commentService.createComment(taskId, request, userId);

        // then
        assertNotNull(response);
        assertEquals("대댓글 내용", response.content());
        assertEquals("김철수", response.user().name());
    }

    @Test
    void getComments_부모와_자식_댓글_조회_성공() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long parentId = 3L;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTask(user);
        Comment parentComment = CommentFixture.createCommentWithId("부모 댓글", task, user, parentId);
        Comment childComment = CommentFixture.createComment("자식 댓글", task, user);

        UserResponse userResponse = UserFixture.createUserResponse(user, userId);
        CommentResponse parentResponse = CommentFixture
                .createCommentResponse(10L, "부모 댓글", taskId, userId, userResponse);
        CommentResponse childResponse = CommentFixture
                .createCommentResponse(11L, "자식 댓글", taskId, userId, userResponse);

        Page<Comment> parentComments = new PageImpl<>(List.of(parentComment), pageable, 1);

        when(taskService.findByTaskId(taskId)).thenReturn(task);
        when(commentRepository.findByTask_TaskIdAndParentIdIsNull(taskId, pageable)).thenReturn(parentComments);
        when(userService.toUserResponse(user)).thenReturn(userResponse);
        when(commentMapper.toCommentResponse(parentComment, userResponse)).thenReturn(parentResponse);

        when(commentRepository.findByParentId(parentComment.getCommentId(), sort)).thenReturn(List.of(childComment));
        when(commentMapper.toCommentResponse(childComment, userResponse)).thenReturn(childResponse);

        // when
        Page<CommentResponse> comments = commentService.getComments(taskId, pageable, "newest");

        // then
        assertThat(comments).isNotNull();
        assertThat(comments.getContent()).hasSize(2);
        assertThat(comments.getContent().get(0).content()).isEqualTo("부모 댓글");
        assertThat(comments.getContent().get(1).content()).isEqualTo("자식 댓글");
    }

    @Test
    void updateComment_댓글_수정_성공() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long commentId = 3L;
        CommentUpdateRequest request = new CommentUpdateRequest("수정된 내용");

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUserWithId(team, userId);
        Task task = TaskFixture.createTaskWithId(user, taskId);
        Comment comment = CommentFixture.createComment("원래 내용", task, user);

        UserResponse userResponse = UserFixture.createUserResponse(user, userId);
        CommentResponse expectedResponse = CommentFixture.createCommentResponse(commentId, "수정된 내용", taskId, userId, userResponse);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(taskService.findByTaskId(taskId)).thenReturn(task);
        when(userService.toUserResponse(user)).thenReturn(userResponse);
        when(commentMapper.toCommentResponse(comment, userResponse)).thenReturn(expectedResponse);

        // when
        CommentResponse response = commentService.updateComment(taskId, request, commentId, userId);

        // then
        assertNotNull(response);
        assertEquals("수정된 내용", response.content());
    }

    @Test
    void updateComment_댓글이_존재하지_않는_경우() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long commentId = 3L;
        CommentUpdateRequest request = new CommentUpdateRequest("수정된 내용");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommentException.class, () -> commentService.updateComment(taskId, request, commentId, userId));
    }

    @Test
    void updateComment_이미_삭제된_댓글인_경우() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long commentId = 3L;
        CommentUpdateRequest request = new CommentUpdateRequest("수정된 내용");

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUser(team);
        Task task = TaskFixture.createTask(user);
        Comment comment = CommentFixture.createComment("삭제된 댓글", task, user);
        comment.delete(); // 삭제 처리

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(taskService.findByTaskId(taskId)).thenReturn(task);

        // when & then
        assertThrows(CommentException.class, () -> commentService.updateComment(taskId, request, commentId, userId));
    }

    @Test
    void updateComment_작성자가_아닌_경우() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long commentId = 3L;
        CommentUpdateRequest request = new CommentUpdateRequest("수정된 내용");

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUserWithId(team, 100L);
        Task task = TaskFixture.createTaskWithId(user, taskId);
        Comment comment = CommentFixture.createComment("원래 내용", task, user);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(taskService.findByTaskId(taskId)).thenReturn(task);

        // when & then
        assertThrows(CommentException.class, () -> commentService.updateComment(taskId, request, commentId, userId));
    }

    @Test
    void deleteComment_댓글_삭제_성공_자식댓글_없음() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long commentId = 3L;

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUserWithId(team, userId);
        Task task = TaskFixture.createTaskWithId(user, taskId);
        Comment comment = CommentFixture.createCommentWithId("댓글 내용", task, user, commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(taskService.findByTaskId(taskId)).thenReturn(task);
        when(commentRepository.findByParentId(commentId, Sort.unsorted())).thenReturn(List.of());

        // when
        int deletedCount = commentService.deleteComment(taskId, commentId, userId);

        // then
        assertEquals(1, deletedCount);
        assertTrue(comment.isDeleted());
    }

    @Test
    void deleteComment_댓글_삭제_성공_자식댓글_포함() {

        // given
        Long taskId = 1L;
        Long userId = 2L;
        Long commentId = 3L;

        Team team = TeamFixture.createTeam();
        User user = UserFixture.createUserWithId(team, userId);
        Task task = TaskFixture.createTaskWithId(user, taskId);
        Comment parentComment = CommentFixture.createCommentWithId("부모 댓글", task, user, commentId);
        Comment childComment1 = CommentFixture.createComment("대댓글1", task, user);
        Comment childComment2 = CommentFixture.createComment("대댓글2", task, user);
        ReflectionTestUtils.setField(childComment1, "parentId", commentId);
        ReflectionTestUtils.setField(childComment2, "parentId", commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(parentComment));
        when(taskService.findByTaskId(taskId)).thenReturn(task);
        when(commentRepository.findByParentId(commentId, Sort.unsorted()))
                .thenReturn(List.of(childComment1, childComment2));

        // when
        int deletedCount = commentService.deleteComment(taskId, commentId, userId);

        // then
        assertEquals(3, deletedCount);
        assertTrue(parentComment.isDeleted());
        assertTrue(childComment1.isDeleted());
        assertTrue(childComment2.isDeleted());
    }
}
