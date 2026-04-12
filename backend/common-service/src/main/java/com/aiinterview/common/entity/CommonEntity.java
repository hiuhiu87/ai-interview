package com.aiinterview.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class CommonEntity {

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "updated_date")
    private Long updatedDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @PrePersist
    private void prePersist() {
        long currentTime = System.currentTimeMillis();
        this.createdDate = currentTime;
        this.updatedDate = currentTime;
        if (this.deleted == null) {
            this.deleted = false;
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDate = System.currentTimeMillis();
    }

}
