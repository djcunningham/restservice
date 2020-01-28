package com.example.business.models;

import com.example.business.RoleNames;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
public class UserModel {
    @NonNull private String userName;
    @NonNull private String firstName;
    @NonNull private String lastName;

    @RoleNames
    private List<String> roles;
}
