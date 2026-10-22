package com.smartfactory.factory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="factories")
public class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 100)
    private String name;
    @Column(nullable = false)
    private String code;
    @Column(length = 200)
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private FactoryStatus status;
    @Column(updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name="updated_at",nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
