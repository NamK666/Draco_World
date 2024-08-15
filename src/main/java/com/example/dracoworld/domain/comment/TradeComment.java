package com.example.dracoworld.domain.comment;

import com.example.dracoworld.domain.baseentity.CommentBaseEntity;
import com.example.dracoworld.domain.board.Trade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "trade_comment")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TradeComment extends CommentBaseEntity {

	// 댓글 id (pk)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키가 자동으로 생성되도록 설정 , db에 의해 자동증가 되는 id값을 사용한다는 의미
	@Column // 해당 필드가 db의 컬럼임을 명시
	private Long id;

	// 게시물 id (fk)
	@ManyToOne(fetch = FetchType.LAZY) // TradeComment 엔티티가 Trade 엔티티와 다대일 관계에 있음을 나타냄.
	@JoinColumn(name = "trade_id", nullable = false) // 'trade_id'라는 이름의 외래키로서 Trade 엔티티를 참조하도록 지정한다.
	private Trade trade;
}
