package com.ssafy.myalbba.notice.entity;

import com.ssafy.myalbba.Member.entity.Owner;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    private Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Notice createNotice(String title, String content) {
        return new Notice(title, content);
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }
}