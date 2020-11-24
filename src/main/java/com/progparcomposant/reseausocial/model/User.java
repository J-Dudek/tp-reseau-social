package com.progparcomposant.reseausocial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private Date birthdate;
    private String email;
    private String phoneNumber;
    private String city;
    private Timestamp signInDate;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;
}
