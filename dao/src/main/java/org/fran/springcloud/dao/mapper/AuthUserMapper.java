package org.fran.springcloud.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AuthUserMapper {

    @Select("SELECT id, name, account, email, phone, password, status FROM auth_user")
    public List<Map> select();
}