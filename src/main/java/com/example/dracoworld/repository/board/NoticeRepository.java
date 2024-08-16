package com.example.dracoworld.repository.board;

import com.example.dracoworld.domain.Category;
import com.example.dracoworld.domain.board.Notice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	List<Notice> findAllByStatusIsTrueOrderByCreatedAtDesc();

	Optional<Notice> findByIdAndStatusIsTrue(Long id);

	List<Notice> findTop5ByCategoryAndStatusIsTrueOrderByCreatedAtDesc(Category category);

}
