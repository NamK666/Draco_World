package com.example.dracoworld.service;

import com.example.dracoworld.aop.AuthCheck;
import com.example.dracoworld.aop.AuthorType;
import com.example.dracoworld.domain.Category;
import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.board.Notice;
import com.example.dracoworld.domain.comment.NoticeComment;
import com.example.dracoworld.dto.board.NoticeDto;
import com.example.dracoworld.dto.comment.NoticeCommentDto;
import com.example.dracoworld.repository.board.NoticeRepository;
import com.example.dracoworld.repository.comment.NoticeCommentRepository;
import com.example.dracoworld.security.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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

	/* 공지 게시글 조회 */
	public Optional<NoticeDto> getNoticeDetail(Long id) {
		return noticeRepository.findById(id).map(NoticeDto::convertToDto);
	}

	/* 공지 게시글 수정 */
	@AuthCheck(value = {
		"ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.POST)
	@Transactional
	public void editNotice(Long id, NoticeDto noticeDto) {
		Notice.validateField(noticeDto.getTitle(), "Title");
		Notice.validateField(noticeDto.getContent(), "Content");

		Notice notice = findByIdAndStatusIsTrue(id);
		notice.update(noticeDto);
	}

	/* 공지사항 게시글 삭제 */
	@AuthCheck(value = {
		"ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.POST)
	@Transactional
	public void deleteNotice(Long id) {
		Notice notice = findByIdAndStatusIsTrue(id);
		notice.delete();
	}

	/* 공지사항 댓글 조회 */
	public Optional<List<NoticeCommentDto>> getNoticeCommentList(
		Long id) {  // Optional: 결과가 없을 수 있음을 명시
		List<NoticeCommentDto> noticeCommentDtoList
			= noticeCommentRepository.findAllByCommentStatusIsTrueAndNoticeId(id).stream()
			.map(NoticeCommentDto::convertToDto)
			.toList(); // map 연산으로 각 댓글 엔티티를 NoticeCommentDto 로 변환 후에, toList()로 결과를 List 형태로 수집
		return noticeCommentDtoList.isEmpty() ? Optional.empty()
			: Optional.of(noticeCommentDtoList);
	}

	/* 공지사항 댓글 작성 */
	@AuthCheck(value = {"ADMIN", "NORMAL"}, Type = "Notice", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void createNoticeComment(Long id, NoticeCommentDto noticeCommentDto) {
		// 입력 유효성 검사
		Notice.validateField(noticeCommentDto.getContent(), "Content");

		//현재 로그인한 사용자 정보 가져오기
		Member currentUser = currentUserProvider.getCurrentUser();

		//댓글 DTO 정보 설정
		noticeCommentDto.setAuthorEmail(currentUser.getEmail());
		noticeCommentDto.setCommentStatus(Boolean.TRUE);
		noticeCommentDto.setCreatedAt(LocalDateTime.now());

		//공지사항 조회 (주어진 id로 활성 상태의 공지사항을 조회)
		Notice notice = findByIdAndStatusIsTrue(id);

		//댓글 Entity 생성 및 저장
		NoticeComment noticeComment = NoticeCommentDto.convertToEntity(noticeCommentDto, notice,
			currentUser); // DTO를 Entity로 변환한다. 이때, 공지사항과 현재 사용자 정보도 함께 전달
		noticeCommentRepository.save(noticeComment); // 생성된 댓글 Entity를 저장소에 저장
	}

	/* 공지사항 댓글 수정 */
	@AuthCheck(value = {"NORMAL",
		"ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void editNoticeComment(Long postId, Long id,
		NoticeCommentDto noticeCommentDto) {
		Notice.validateField(noticeCommentDto.getContent(), "Content");

		findByIdAndStatusIsTrue(postId);
		NoticeComment noticeComment = findByIdAndCommentStatusIsTrue(id);
		noticeComment.updateComment(noticeCommentDto);
	}

	/* 공지사항 댓글 삭제 */
	@AuthCheck(value = {"NORMAL",
		"ADMIN"}, checkAuthor = true, Type = "Notice", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void deleteNoticeComment(Long id) {
		NoticeComment noticeComment = findByIdAndCommentStatusIsTrue(id);
		noticeComment.deleteComment();
	}

	/* 카테고리 별로 상위 5개 게시글*/
	public Optional<List<NoticeDto>> getNoticeListByCategory(Category category) {
		List<NoticeDto> getNotice = noticeRepository.findTop5ByCategoryAndStatusIsTrueOrderByCreatedAtDesc(
				category) // 카테고리와 상태가 true인 공지를 최신순으로 5개 가져오기
			.stream() // 가져온 목록을 Stream api로 처리
			.map(NoticeDto::convertToDto) // Notice 엔티티 객체를 DTO로 변환
			.collect(Collectors.toList()); // 변환된 DTO 객체들을 List로 수집

		return getNotice.isEmpty() ? Optional.empty() : Optional.of(getNotice);
	}


	/* 공지사항 찾기 메소드 */
	private Notice findByIdAndStatusIsTrue(Long id) {
		return noticeRepository.findByIdAndStatusIsTrue(id)
			.orElseThrow(() -> new EntityNotFoundException("공지사항을 찾지 못하였습니다."));
	}

	/* 공지사항 댓글 찾기 메소드 */
	private NoticeComment findByIdAndCommentStatusIsTrue(Long id) {
		return noticeCommentRepository.findByIdAndCommentStatusIsTrue(id)
			.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
	}

	// 글 작성자가 현재 로그인한 사용자인지 확인 //
	@Override
	public boolean isPostAuthor(Long postId, String memberEmail) {
		return noticeRepository.findById(postId)
			.map(notice -> notice.isAuthor(memberEmail))
			.orElse(false);
	}

	// 댓글 작성자가 현재 로그인한 사용자인지 확인 //
	@Override
	public boolean isCommentAuthor(Long commentId, String memberEmail) {
		return noticeCommentRepository.findById(commentId)
			.map(notice -> notice.isAuthor(memberEmail))
			.orElse(false);
	}
}