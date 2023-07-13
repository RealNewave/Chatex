package com.devex.responder;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class ResponderTokenEntity extends PanacheEntityBase {

    @Id
    @UuidGenerator
    private UUID token;

    @CreationTimestamp
    private ZonedDateTime creation;
    @UpdateTimestamp
    private ZonedDateTime updated;
}
