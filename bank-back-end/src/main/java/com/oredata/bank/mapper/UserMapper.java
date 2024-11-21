package com.oredata.bank.mapper;

import com.oredata.bank.dto.UserDTO;
import com.oredata.bank.user.model.UserModel;
import com.oredata.bank.utils.PasswordEncoderMapping;
import com.oredata.bank.utils.PasswordMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",uses = PasswordEncoderMapping.class)
public interface UserMapper {

    @Mapping(target = "accounts",source = "accountDTOS")
    @Mapping(target = "password",source = "password",qualifiedBy = PasswordMapping.class)
    UserModel toUserModel(UserDTO userDTO);

    @Mapping(target = "accountDTOS",source = "accounts")
    @Mapping(target = "password",ignore = true,source = "password")
    UserDTO toUserDTO(UserModel userModel);
}


