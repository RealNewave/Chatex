package com.devex.question;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class QuestionEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public UUID id;

    private String starter;
    private String question;
    @OneToMany
    private List<AnswerEntity> answers;
    @CreationTimestamp
    private ZonedDateTime timestamp;
    @UpdateTimestamp
    private ZonedDateTime updated;

}
