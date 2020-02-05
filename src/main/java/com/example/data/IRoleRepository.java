package com.example.data;

import com.example.data.entity.RoleEntity;
import com.example.data.entity.UserEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface IRoleRepository extends CrudRepository<RoleEntity, Integer> {

  Optional<RoleEntity> findByName(String name);

  Boolean existsByName(String name);

}
