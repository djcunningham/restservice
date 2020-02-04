package com.example.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "[Role]",
        indexes = { @Index(name = "IX_Role_Name", columnList = "Name", unique = true) }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude="users")
@ToString(exclude = "users")
public class RoleEntity {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Column(name="Id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "Name", nullable = false, unique =  true, length = 50)
  @NaturalId
  @NonNull
  private String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private Set<UserEntity> users = new HashSet<UserEntity>();
}

