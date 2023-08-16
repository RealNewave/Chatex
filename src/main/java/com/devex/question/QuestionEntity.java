package com.devex.question;

import com.devex.responder.ResponderEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
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
    @OneToMany
    private List<ResponderEntity> responders;
    @CreationTimestamp
    private ZonedDateTime timestamp;
    @UpdateTimestamp
    private ZonedDateTime updated;

    private boolean answered;

    @Column(name = "open_to_public")
    private boolean openToPublic;

}
