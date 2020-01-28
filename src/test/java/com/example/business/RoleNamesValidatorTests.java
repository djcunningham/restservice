package com.example.business;

import com.example.data.IRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoleNamesValidatorTests {

  @Mock
  private IRoleRepository roleRepository;

  @Mock
  private ConstraintValidatorContext constraintValidatorContext;

  @Mock
  ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

  @Test
  public void isValidReturnsTrueForAllValidRoleNames() {
    when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
    when(roleRepository.existsByName(anyString())).thenReturn(true);
    RoleNamesValidator validator = new RoleNamesValidator(this.roleRepository);
    List<String> roles = Arrays.asList("Role1");
    Boolean isValid = validator.isValid(roles, this.constraintValidatorContext);
    verify(constraintValidatorContext, times(0)).buildConstraintViolationWithTemplate(anyString());
    assertThat(isValid).isEqualTo(true);
  }

  @Test
  public void isValidReturnsFalseForAllInvalidRoleNames() {
    when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
    when(roleRepository.existsByName(anyString())).thenReturn(false);
    RoleNamesValidator validator = new RoleNamesValidator(this.roleRepository);
    List<String> roles = Arrays.asList("BadRole1", "BadRole2");
    Boolean isValid = validator.isValid(roles, this.constraintValidatorContext);
    verify(constraintValidatorContext, times(2)).buildConstraintViolationWithTemplate(anyString());
    verify(constraintValidatorContext, times(1)).buildConstraintViolationWithTemplate("Role = BadRole1 is not valid");
    verify(constraintValidatorContext, times(1)).buildConstraintViolationWithTemplate("Role = BadRole2 is not valid");
    assertThat(isValid).isEqualTo(false);
  }

  @Test
  public void isValidReturnsFalseForValidAndInvalidRoleNames() {
    when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
    when(roleRepository.existsByName("Role1")).thenReturn(true);
    when(roleRepository.existsByName("BadRole1")).thenReturn(false);
    RoleNamesValidator validator = new RoleNamesValidator(this.roleRepository);
    List<String> roles = Arrays.asList("Role1", "BadRole1");
    Boolean isValid = validator.isValid(roles, this.constraintValidatorContext);
    verify(constraintValidatorContext, times(1)).buildConstraintViolationWithTemplate(anyString());
    verify(constraintValidatorContext, times(1)).buildConstraintViolationWithTemplate("Role = BadRole1 is not valid");
    assertThat(isValid).isEqualTo(false);
  }
}
