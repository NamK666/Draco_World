package com.example.dracoworld.service;

import com.example.dracoworld.aop.AuthCheck;
import com.example.dracoworld.aop.AuthorType;
import com.example.dracoworld.domain.Member;
import com.example.dracoworld.domain.board.Trade;
import com.example.dracoworld.domain.comment.TradeComment;
import com.example.dracoworld.dto.board.TradeDto;
import com.example.dracoworld.dto.comment.TradeCommentDto;
import com.example.dracoworld.repository.board.TradeRepository;
import com.example.dracoworld.repository.comment.TradeCommentRepository;
import com.example.dracoworld.security.CurrentUserProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기전용 트랜잭션으로 동작
@Service
public class TradeService implements BoardService {

	private final TradeRepository tradeRepository;
	private final CurrentUserProvider currentUserProvider;
	private final TradeCommentRepository tradeCommentRepository;


	/* 거래 게시판 글 목록 조회 */
	@AuthCheck(value = {"NORMAL", "ADMIN"}, Type = "Trade", AUTHOR_TYPE = AuthorType.POST)
	// NORMAL, ADMIN 권한을 가진 사용자만 이 메소드에 접근이 가능하다.
	// 권한 검사의 대상이 "Trade"엔티티임을 나타낸다.
	// AuthorType.POST 는 '게시글'에 대한 권한 검사임을 나타낸다.
	public Optional<List<TradeDto>> getAllTrades() {
		List<TradeDto> trades = tradeRepository.findByDeletedAtIsNull().stream()
			.map(TradeDto::convertToDto) // 각 Trade 엔티티를 TradeDto 객체로 변환하는 과정
			.collect(Collectors.toList()); // 변환된 TradeDto 객체들을 다시 List로 모은다
		return trades.isEmpty() ? Optional.empty() : Optional.of(trades);
	}

	/* 거래 글 상세보기 */
	@AuthCheck(value = {"NORMAL, ADMIN"}, Type = "Trade", AUTHOR_TYPE = AuthorType.POST)
	public Optional<TradeDto> getTradeById(Long id) { // 조회하고자 하는 게시글의 고유 id
		return tradeRepository.findByIdAndStatusIsTrue(id)
			.map(TradeDto::convertToDto); // 조회된 Trade 엔티티가 있다면, 이를 TradeDto 객체로 변환한다.
		// TradeDto 클래스에 정의된 convertToDto 정적 메서드를 호출하는 것
	}

	/* 거래 게시판 글 작성하기 */
	@AuthCheck(value = {"NORMAL", "ADMIN"}, Type = "Trade", AUTHOR_TYPE = AuthorType.POST)
	@Transactional // 여러 db 작업을 하나의 논리적 단위로 묶어준다.
	public void createTradePost(TradeDto tradeDto) {
		// Not Null 예외 처리
		Trade.validateField(tradeDto.getTitle(), "Title");
		Trade.validateField(tradeDto.getContent(), "Content");

		// 현재 로그인된 사용자를 가져온다.
		Member currentUser = currentUserProvider.getCurrentUser();

		// 새로운 거래 게시글을 저장한다.
		tradeRepository.save(TradeDto.convertToEntity(tradeDto, currentUser));
	}

	/* 거래 게시판 글 수정하기 */
	@AuthCheck(value = {"NORMAL",
		"ADMIN"}, checkAuthor = true, Type = "Trade", AUTHOR_TYPE = AuthorType.POST)
	@Transactional
	public void updateTrade(Long id, TradeDto tradeDto) {
		// Not Null 예외 처리
		Trade.validateField(tradeDto.getTitle(), "Title");
		Trade.validateField(tradeDto.getContent(), "Content");

		Trade trade = tradeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("id: " + id + " not found"));
		trade.update(tradeDto);
	}

	/* 거래 게시판 글의 댓글 목록 조회 */
	@AuthCheck(value = {"NORMAL", "ADMIN"}, Type = "Trade", AUTHOR_TYPE = AuthorType.COMMENT)
	public Optional<List<TradeCommentDto>> getCommentsByTradeId(Long tradeId) {
		List<TradeCommentDto> comments = tradeCommentRepository.findAllByTradeIdAndCommentStatusIsTrue(
				tradeId).stream()
			.filter(TradeComment::getCommentStatus)
			.map(TradeCommentDto::convertToDto)
			.collect(Collectors.toList());
		return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
	}

	/* 거래 게시판 글 댓글 작성 */
	@AuthCheck(value = {"NORMAL", "ADMIN"}, Type = "Trade", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void createTradeComment(Long id, TradeCommentDto tradeCommentDto) {
		Member currentUser = currentUserProvider.getCurrentUser();

		// Not Null 예외 처리
		TradeComment.validateField(tradeCommentDto.getContent(), "Content");
		Trade trade = tradeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("id: " + id + " not found"));

		// 댓글 저장
		TradeComment tradeComment = TradeCommentDto.convertToEntity(trade, tradeCommentDto,
			currentUser);
		tradeCommentRepository.save(tradeComment);
	}

	/* 거래 게시판 글 댓글 수정 */
	@AuthCheck(value = {"NORMAL",
		"ADMIN"}, checkAuthor = true, Type = "Trade", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void updateTradeComment(Long id, TradeCommentDto tradeCommentDto) {
		// Not Null 예외 처리
		TradeComment.validateField(tradeCommentDto.getContent(), "Content");
		TradeComment tradeComment = tradeCommentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("id: " + id + " not found"));

		// 댓글 수정 처리
		tradeComment.updateComment(tradeCommentDto);
	}

	/* 거래 게시판 글 댓글 삭제 */
	@AuthCheck(value = {"NORAML",
		"ADMIN"}, checkAuthor = true, Type = "Trade", AUTHOR_TYPE = AuthorType.COMMENT)
	@Transactional
	public void deleteTradeComment(Long id) {
		// Not Null 예외 처리
		TradeComment tradeComment = tradeCommentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("id: " + id + " not found"));

		// 댓글 삭제 처리
		tradeComment.deleteComment();
	}

	/* 주어진 게시글 ID의 작성자가 현재 사용자인지 확인 */
	@Override
	public boolean isPostAuthor(Long postId, String memberEmail) {
		return tradeRepository.findById(postId).map(trade -> trade.isAuthor(memberEmail))
			.orElse(false);
	}

	/* 주어진 댓글 ID의 작성자가 현재 사용자인지 확인 */
	@Override
	public boolean isCommentAuthor(Long commentId, String memberEmail) {
		return tradeCommentRepository.findById(commentId).map(trade -> trade.isAuthor(memberEmail))
			.orElse(false);
	}
}
