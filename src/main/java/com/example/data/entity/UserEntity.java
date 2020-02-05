package com.example.data.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "[User]",
        indexes = {
          @Index(name = "IX_User_UserName", columnList = "UserName", unique = true),
          @Index(name = "IX_User_LastName", columnList = "LastName")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude="roles")
@ToString(exclude = "roles")
public class UserEntity {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="Id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "UserName", nullable = false, unique =  true)
  @NonNull
  private String userName;

  @Column(name = "FirstName", nullable = false, length = 50)
  @NonNull
  private  String firstName;

  @Column(name = "LastName", nullable = false, length = 50)
  @NonNull
  private  String lastName;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
  @JoinTable(
          name = "UserRole",
          joinColumns = @JoinColumn(name = "UserId"),
          inverseJoinColumns = @JoinColumn(name = "RoleId"))
  private Set<RoleEntity> roles = new HashSet<>();

  public void addRole(RoleEntity roleEntity) {
    this.roles.add(roleEntity);
    roleEntity.getUsers().add(this);
  }
}
