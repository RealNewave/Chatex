package com.devex;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class AnswerEntity extends PanacheEntity {
    private String username;
    private String answer;
    @CreationTimestamp
    private ZonedDateTime timestamp;
}
