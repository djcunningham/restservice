package com.example.data;

import com.example.data.entity.RoleEntity;
import com.example.restservice.PersistenceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(
        classes = { PersistenceConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoleRepositoryTests {

  @Autowired
  private IRoleRepository roleRepository;

  @Test
  public void roleRepositoryPersistsRole() {
    RoleEntity roleEntity = new RoleEntity("TestRole");
    roleRepository.save(roleEntity);

    Optional<RoleEntity> retrievedEntity = roleRepository.findById(1);
    assertThat(retrievedEntity.isPresent()).isTrue();
    assertThat(retrievedEntity.get().getId()).isEqualTo(1);
    assertThat(retrievedEntity.get().getName()).isEqualTo("TestRole");
  }

  @Test
  public void saveThrowsExceptionOnInsertDuplicateRoleName() {
    RoleEntity roleEntity = new RoleEntity("TestRole");
    roleRepository.save(roleEntity);

    RoleEntity roleEntity2 = new RoleEntity("TestRole");
    assertThrows(DataIntegrityViolationException.class, () -> roleRepository.save(roleEntity2));
  }
}
