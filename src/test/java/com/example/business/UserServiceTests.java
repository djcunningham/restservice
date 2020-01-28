package com.example.business;

import com.example.business.models.UserModel;

import com.example.data.IRoleRepository;
import com.example.data.IUserRepository;
import com.example.data.entity.RoleEntity;
import com.example.data.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

  @Mock
  private IUserRepository userDao;

  @Mock
  private IRoleRepository roleRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setUp() {
  }

  @Test
  public void findUserReturnsUserModelIfDaoResultNotNull() {
    RoleEntity roleEntity = new RoleEntity(1, "Role1", null);
    UserEntity entity = new UserEntity(1, "deric.cunningham", "Deric", "Cunningham", Set.of(roleEntity));
    Optional<UserEntity> optionalEntity = Optional.of(entity);
    UserModel expectedUserModel = new UserModel("deric.cunningham", "Deric", "Cunningham", List.of("Role1"));
    when(userDao.findById(1)).thenReturn(optionalEntity);

    UserModel userModel = userService.findUser(1).get();
    assertThat(userModel).isEqualToComparingFieldByField(expectedUserModel);
  }

  @Test
  public void createUserMapsModelToEntity() {
    UserModel userModel = new UserModel("deric.cunningham", "deric", "cunningham", List.of("Role1"));
    when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new RoleEntity(1, "Role1", null)));
    ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
    this.userService.createUser(userModel);
    verify(this.userDao, times(1)).save(userEntityCaptor.capture());
    UserEntity actualEntity = userEntityCaptor.getValue();
    assertThat(actualEntity.getUserName()).isEqualTo("deric.cunningham");
    assertThat(actualEntity.getFirstName()).isEqualTo("deric");
    assertThat(actualEntity.getLastName()).isEqualTo("cunningham");
  }
}
