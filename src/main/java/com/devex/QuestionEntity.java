package com.devex;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class QuestionEntity extends PanacheEntity {
    private String starter;
    private String question;
    @OneToMany
    private List<AnswerEntity> answers;
    @CreationTimestamp
    private ZonedDateTime timestamp;

}
