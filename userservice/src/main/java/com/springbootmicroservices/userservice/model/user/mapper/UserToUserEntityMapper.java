package com.springbootmicroservices.userservice.model.user.mapper;

import com.springbootmicroservices.userservice.model.common.mapper.BaseMapper;
import com.springbootmicroservices.userservice.model.user.User;
import com.springbootmicroservices.userservice.model.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserEntityMapper extends BaseMapper<User, UserEntity> {

    @Override
    UserEntity map(User source);

    static UserToUserEntityMapper initialize() {
        return Mappers.getMapper(UserToUserEntityMapper.class);
    }

}
