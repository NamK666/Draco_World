package com.example.dracoworld.repository.board;

import com.example.dracoworld.domain.board.Trade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

	List<Trade> findByDeletedAtIsNull();

	Optional<Trade> findByIdAndStatusIsTrue(Long id);
}
