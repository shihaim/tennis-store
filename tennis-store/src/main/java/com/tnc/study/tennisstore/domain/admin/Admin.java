package com.tnc.study.tennisstore.domain.admin;

import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.framework.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "admin_id")
    private String id;

    @Embedded
    @AttributeOverride(name = "address", column = @Column(name = "email"))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password", nullable = false))
    private Password password;

    @Column(name = "name", nullable = false)
    private String name;

    public Admin(String id, Email email, Password password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}