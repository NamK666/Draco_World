package com.example.dracoworld.service;

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
public class TradeService implements BoardService{
	private final TradeRepository tradeRepository;


	/* 거래 게시판 글 목록 조회 */

public Optional<List<TradeDto>> getAllTrades() {
	List<TradeDto> trades = tradeRepository.findByDeletedAtIsNull().stream()
		.map(TradeDto::convertToDto)
		.collect(Collectors.toList());
	return trades.isEmpty() ? Optional.empty() : Optional.of(trades);
}
}
