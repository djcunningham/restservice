package com.example.business;

import com.example.data.IRoleRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleNamesValidator implements ConstraintValidator<RoleNames, List<String>> {

  private final IRoleRepository roleRepository;

  public RoleNamesValidator(IRoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public void initialize(RoleNames constraintAnnotation) {

  }

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    Boolean isValid = true;
    List<String> invalidRoleNames = value
            .stream()
            .filter(r -> roleRepository.existsByName(r) == false)
            .collect(Collectors.toList());

    if(invalidRoleNames.size() == 0) {
      return true;
    }
    else {
      context.disableDefaultConstraintViolation();
      for(String invalidRoleName : invalidRoleNames) {
        context
                .buildConstraintViolationWithTemplate(String.format("Role = %s is not valid", invalidRoleName))
                .addConstraintViolation();
      }

      return false;
    }
  }
}
