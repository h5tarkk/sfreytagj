package cn.iocoder.mall.admin.dao;

import cn.iocoder.mall.admin.dataobject.OAuth2RefreshTokenDO;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2RefreshTokenMapper {

    void insert(OAuth2RefreshTokenDO entity);

}