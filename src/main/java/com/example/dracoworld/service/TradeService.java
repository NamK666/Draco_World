package com.example.dracoworld.service;

import com.example.dracoworld.aop.AuthCheck;
import com.example.dracoworld.aop.AuthorType;
import com.example.dracoworld.dto.TradeDto;
import com.example.dracoworld.repository.TradeRepository;
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

}
