package com.example.restservice;

import com.example.business.IUserService;
import com.example.business.RoleNames;
import com.example.business.models.UserModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@RestController
@Api
@Validated
@Log
public class UserController extends ValidatedController {

  private final IUserService userService;

  @Autowired
  public UserController(IUserService userService) {
    this.userService = userService;
  }

  @GetMapping("/user")
  @ApiOperation("Gets a user based on the provided Id")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "id",
                  value = "The Id for the user to retrieve",
                  required = true,
                  dataType = "int",
                  paramType = "query",
                  defaultValue = "0"),
  })
  public Optional<UserModel> user(
          @RequestParam(value = "id")
          @Min(message = "id must be > 0", value = 0)
                  Integer id) {
    log.info(String.format("Getting User object for User Id = %s", id));
    return this.userService.findUser(id);
  }

  @GetMapping("/userByUserName")
  @ApiOperation("Gets a user based on the provided Name")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "userName",
                  value = "The userName for the user to retrieve",
                  required = true,
                  dataType = "string",
                  paramType = "query"),
  })
  public Optional<UserModel> user(@RequestParam(value = "userName") String userName) {
    return this.userService.findUserByUserName(userName);
  }

  @GetMapping("/userByLastName")
  @ApiOperation("Gets users with the provided Last Name")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "lastName",
                  value = "The last name for the user to retrieve",
                  required = true,
                  dataType = "string",
                  paramType = "query"),
  })
  public Iterable<UserModel> userByLastName(@RequestParam(value = "lastName") String lastName) {
    return this.userService.findUserByLastName(lastName);
  }

  @PostMapping("/user")
  @ApiOperation("Creates the user with the provided information")
  public void createUser(@Valid @RequestBody() UserModel userModel) {
    this.userService.createUser(userModel);
  }
}