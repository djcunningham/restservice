package com.example.business;

import com.example.business.models.UserModel;
import com.example.data.IRoleRepository;
import com.example.data.IUserRepository;
import com.example.data.entity.RoleEntity;
import com.example.data.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserService implements IUserService {

  private final IUserRepository userRepository;
  private IRoleRepository roleRepository;

  public UserService(IUserRepository userRepository, IRoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public Optional<UserModel> findUser(Integer id) {
    return this.userRepository
          .findById(id)
          .map(e -> new UserModel(e.getUserName(),
                  e.getFirstName(),
                  e.getLastName(),
                  e.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList())
                  ));
  }

  @Override
  public Optional<UserModel> findUserByUserName(String userName) {
    return this.userRepository
      .findByUserName(userName)
      .map(e -> new UserModel(e.getUserName(), e.getFirstName(), e.getLastName()));
  }

  @Override
  public Iterable<UserModel> findUserByLastName(String lastName) {
    return StreamSupport.stream(
            userRepository
                    .findByLastName(lastName)
                    .spliterator(),
            false)
            .map(e -> new UserModel(
                    e.getUserName(),
                    e.getFirstName(),
                    e.getLastName(),
                    e.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList())))
            .collect(Collectors.toList());
  }

  @Override
  public void createUser(UserModel userModel) {
    UserEntity entity = new UserEntity(userModel.getUserName(), userModel.getFirstName(), userModel.getLastName());
    Set<RoleEntity> roleEntities = userModel
            .getRoles()
            .stream()
            .map(r -> roleRepository.findByName(r))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    entity.setRoles(roleEntities);

    this.userRepository.save(entity);
  }
}