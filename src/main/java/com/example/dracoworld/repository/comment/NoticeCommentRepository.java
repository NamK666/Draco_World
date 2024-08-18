package com.example.dracoworld.repository.comment;

import com.example.dracoworld.domain.comment.NoticeComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {

	List<NoticeComment> findAllByCommentStatusIsTrueAndNoticeId(Long id);

	Optional<NoticeComment> findByIdAndCommentStatusIsTrue(Long id);
}
