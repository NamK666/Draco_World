package com.example.dracoworld.domain.baseentity;

import com.example.dracoworld.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // 이 클래스가 상속용 기본 엔티티임을 나타냄
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 엔티티의 생성일, 수정일을 자동으로 관리
public abstract class BaseEntity {

	// 회원 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	// 제목
	@Column(nullable = false)
	private String title;

	// 내용
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	// 게시글이 삭제되었는지 여부 (Soft Delete)
	@Column(nullable = false)
	private Boolean status;

	// 게시글 생성일
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// 게시글 수정일
	@LastModifiedDate
	@Column
	private LocalDateTime updatedAt;

	// 게시글 삭제일
	@Column
	private LocalDateTime deletedAt;

	/**
	 * Not Null 예외처리 로직
	 */
	public static void validateField(String field, String fieldName) {
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException(fieldName + " cannot be null or empty");
		}
	}

	/**
	 * Dirty Checking 방식 수정 로직
	 * update() 메소드에서 필드값만 변경하면 JPA가 자동으로 변경을 감지하고 데이터베이스에 반영
	 */
	public <T extends Updatable> void update(T dto) {
		validateField(dto.getTitle(), "Title");
		validateField(dto.getContent(), "Content");

		this.title = dto.getTitle();
		this.content = dto.getContent();
	}

	/**
	 * Soft Delete을 위한 Dirty Checking 방식 삭제 로직
	 */
	public void delete() {
		this.status = Boolean.FALSE;
		this.deletedAt = LocalDateTime.now();
	}

	/**
	 * 작성자 확인 메소드
	 */
	public boolean isAuthor(String memberEmail) {
		return this.member.getEmail().equals(memberEmail);
	}

}
