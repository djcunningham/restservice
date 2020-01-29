package com.example.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RoleRepositoryTests {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private IRoleRepository roleRepository;

  @Test
  public void existByNameReturnsTrue() {
    Boolean roleExists = this.roleRepository.existsByName(("Admin"));
    assertThat(roleExists).isTrue();
  }

  @Test
  public void existByNameReturnsFalse() {
    Boolean roleExists = this.roleRepository.existsByName(("Role99"));
    assertThat(roleExists).isFalse();
  }
}
