package cn.iocoder.mall.system.biz.convert;

import cn.iocoder.mall.system.biz.bo.account.AccountBO;
import cn.iocoder.mall.system.biz.dataobject.account.AccountDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountConvert {

    AccountConvert INSTANCE = Mappers.getMapper(AccountConvert.class);

    AccountBO convert(AccountDO accountDO);

}
