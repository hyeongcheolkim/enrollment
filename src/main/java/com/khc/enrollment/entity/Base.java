package com.khc.enrollment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor
public class Base {

    @Column(updatable = false)
    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdTime = now;
        updatedTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedTime = LocalDateTime.now();
    }
}