package com.example.data;

import com.example.data.entity.RoleEntity;
import com.example.data.entity.UserEntity;
import com.example.restservice.PersistenceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.management.relation.InvalidRoleInfoException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ContextConfiguration(
        classes = { PersistenceConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTests {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Test
    public void userRepositorySavesUserWithNoRole() {
        UserEntity userEntity = new UserEntity("djcunningham", "Deric", "Cunningham");
        userRepository.save(userEntity);

        Optional<UserEntity> retrievedEntity = userRepository.findById(1);
        assertThat(retrievedEntity.isPresent()).isTrue();
        assertThat(retrievedEntity.get().getUserName()).isEqualTo("djcunningham");
        assertThat(retrievedEntity.get().getFirstName()).isEqualTo("Deric");
        assertThat(retrievedEntity.get().getLastName()).isEqualTo("Cunningham");
    }

    @Test
    public void userRepositorySavesUserWithExistingRole() {
        List<RoleEntity> roleEntities = List.of(new RoleEntity("Admin"), new RoleEntity("NonAdmin"));
        roleRepository.saveAll(roleEntities);
        UserEntity userEntity = new UserEntity("djcunningham", "Deric", "Cunningham");
        userEntity.addRole(roleEntities.get(0));
        userRepository.save(userEntity);

        Optional<UserEntity> retrievedEntity = userRepository.findById(1);
        assertThat(retrievedEntity.isPresent()).isTrue();
        assertThat(retrievedEntity.get().getUserName()).isEqualTo("djcunningham");
        assertThat(retrievedEntity.get().getFirstName()).isEqualTo("Deric");
        assertThat(retrievedEntity.get().getLastName()).isEqualTo("Cunningham");

        List<RoleEntity> userRoles = retrievedEntity.get().getRoles().stream().collect(Collectors.toList());
        assertThat(userRoles.size()).isEqualTo(1);
        assertThat(userRoles.get(0).getName()).isEqualTo("Admin");
    }

    @Test
    public void saveThrowsExceptionOnDuplicateUserName() {
        UserEntity userEntity = new UserEntity("djcunningham", "Deric", "Cunningham");
        userRepository.save(userEntity);

        UserEntity userEntity2 = new UserEntity("djcunningham", "Deric", "Cunningham");
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(userEntity2));
    }
}
