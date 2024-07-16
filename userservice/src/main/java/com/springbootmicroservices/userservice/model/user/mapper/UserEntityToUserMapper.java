package com.springbootmicroservices.userservice.model.user.mapper;

import com.springbootmicroservices.userservice.model.common.mapper.BaseMapper;
import com.springbootmicroservices.userservice.model.user.User;
import com.springbootmicroservices.userservice.model.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityToUserMapper extends BaseMapper<UserEntity, User> {

    @Override
    User map(UserEntity source);

    static UserEntityToUserMapper initialize() {
        return Mappers.getMapper(UserEntityToUserMapper.class);
    }

}
