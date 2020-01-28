package com.example.business;

import com.example.business.models.UserModel;
import java.util.List;
import java.util.Optional;

public interface IUserService {
  Optional<UserModel> findUser(Integer id);

  Optional<UserModel> findUserByUserName(String lastName);

  Iterable<UserModel> findUserByLastName(String userName);

  void createUser(UserModel userModel);
}
