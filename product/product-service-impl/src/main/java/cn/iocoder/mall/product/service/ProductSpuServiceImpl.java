package cn.iocoder.mall.product.service;

import cn.iocoder.common.framework.vo.CommonResult;
import cn.iocoder.mall.product.api.ProductSpuService;
import cn.iocoder.mall.product.api.bo.ProductSpuBO;
import cn.iocoder.mall.product.api.bo.ProductSpuDetailBO;
import cn.iocoder.mall.product.api.dto.ProductSpuAddDTO;
import cn.iocoder.mall.product.api.dto.ProductSpuUpdateDTO;
import cn.iocoder.mall.product.convert.ProductSpuConvert;
import cn.iocoder.mall.product.dao.ProductSpuMapper;
import cn.iocoder.mall.product.dataobject.ProductSpuDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 实际上不用添加。添加的原因是，必须 Spring 报错提示
@com.alibaba.dubbo.config.annotation.Service
public class ProductSpuServiceImpl implements ProductSpuService {

    @Autowired
    private ProductSpuMapper productSpuDAO;

    @Override
    public ProductSpuBO getProductSpu(Integer id) {
        ProductSpuDO productSpuDO = productSpuDAO.selectById(id);
        // 转换成 BO
        return ProductSpuConvert.INSTANCE.convert(productSpuDO);
    }

    @Override
    public CommonResult<ProductSpuDetailBO> addProductSpu(Integer adminId, ProductSpuAddDTO productSpuAddDTO) {
        return null;
    }

    @Override
    public CommonResult<Boolean> updateProductSpu(Integer adminId, ProductSpuUpdateDTO productSpuUpdateDTO) {
        return null;
    }

}