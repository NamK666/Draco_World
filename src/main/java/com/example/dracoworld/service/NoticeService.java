package com.example.dracoworld.service;

import com.example.dracoworld.aop.AuthCheck;
import com.example.dracoworld.aop.AuthorType;
import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.board.Notice;
import com.example.dracoworld.dto.board.NoticeDto;
import com.example.dracoworld.repository.board.NoticeRepository;
import com.example.dracoworld.repository.comment.NoticeCommentRepository;
import com.example.dracoworld.security.CurrentUserProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService implements BoardService {

	private final NoticeRepository noticeRepository;
	private final NoticeCommentRepository noticeCommentRepository;
	private final CurrentUserProvider currentUserProvider;

	public Optional<List<NoticeDto>> getAllNotices() {
		List<NoticeDto> notices = noticeRepository.findAllByStatusIsTrueOrderByCreatedAtDesc()
			.stream()
			.map(NoticeDto::convertToDto)
			.collect(Collectors.toList());
		return notices.isEmpty() ? Optional.empty() : Optional.of(notices);
	}

	/* 공지 게시글 작성 */
	@AuthCheck(value = {"ADMIN"}, Type = "Notice", AUTHOR_TYPE = AuthorType.POST)
	@Transactional
	public void createNotice(NoticeDto noticeDto) {
		Notice.validateField(noticeDto.getTitle(), "Title");
		Notice.validateField(noticeDto.getContent(), "Content");

		Member currentUser = currentUserProvider.getCurrentUser();
		noticeDto.setAuthorName(currentUser.getNickname());
		noticeDto.setStatus(true);
		Notice notice = NoticeDto.convertToEntity(noticeDto, currentUser);
		noticeRepository.save(notice);
	}

	public Optional<NoticeDto> getNoticeDetail(Long id) {
		return noticeRepository.findById(id).map(NoticeDto::convertToDto);
	}
}
