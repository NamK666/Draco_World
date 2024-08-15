package com.example.dracoworld.repository.comment;

import com.example.dracoworld.domain.comment.TradeComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeCommentRepository extends JpaRepository<TradeComment, Long> {

	List<TradeComment> findAllByTradeIdAndCommentStatusIsTrue(Long tradeId);
}
