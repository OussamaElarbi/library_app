package com.collabera.library_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans",
        indexes = {
                @Index(name = "idx_loans_member_id", columnList = "member_id"),
                @Index(name = "idx_loans_book_id", columnList = "book_id"),
                @Index(name = "idx_loans_returned_at", columnList = "returned_at"),
                @Index(name = "idx_loans_member_returned", columnList = "member_id,returned_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(
            name = "booked_at",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime bookedAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;
}