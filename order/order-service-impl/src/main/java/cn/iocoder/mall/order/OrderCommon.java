package cn.iocoder.mall.order;

import cn.iocoder.mall.order.dataobject.OrderItemDO;

import java.util.List;

/**
 * 订单常用
 *
 * @author Sin
 * @time 2019-03-23 11:51
 */
public interface OrderCommon {

    /**
     * 计算订单金额
     *
     * @param items
     * @return
     */
    Integer calculatedAmount(List<OrderItemDO> items);
}
