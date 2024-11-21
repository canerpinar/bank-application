package com.oredata.bank.mapper;

import com.oredata.bank.dto.AccountDTO;
import com.oredata.bank.user.model.AccountModel;
import com.oredata.bank.utils.PasswordEncoderMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper{


    @Mapping(target = "username",source = "user.username")
    AccountDTO toAccount(AccountModel accountModel);

    List<AccountDTO> toAccountDtoList(List<AccountModel> accountModelList);

    AccountModel toAccountDTO(AccountDTO accountDTO);
}