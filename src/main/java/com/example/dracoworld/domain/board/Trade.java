package com.example.dracoworld.domain.board;

import com.example.dracoworld.domain.baseentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "trade") // 이 클래스가 'trade'테이블과 매핑되는 엔티티임을 나타냄
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Trade extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // db에 새로운 레코드를 삽입할 때 고유한 id를 자동으로 생성한다.
	// (주로 @Id와 함께 사용)
	// GenerationType.IDENTITY : db의 자동 증가 컬럼을 사용
	@Column
	private Long id;
}
