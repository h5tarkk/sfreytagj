package cn.iocoder.mall.order.biz.dao;

import cn.iocoder.mall.order.biz.dataobject.OrderItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * 订单 item mapper
 *
 * @author Sin
 * @time 2019-03-16 15:09
 */
@Repository
public interface OrderItemMapper {

    /**
     * 插入数据
     *
     * @param orderItemDO
     */
    void insert(OrderItemDO orderItemDO);

    /**
     * 更新 - 根据Id
     *
     * @param orderItemDO
     */
    void updateById(@Param("orderItemDO") OrderItemDO orderItemDO);

    /**
     * 更新 - 根据 orderId
     * @param orderItemDO
     */
    void updateByOrderId(@Param("orderItemDO") OrderItemDO orderItemDO);

    /**
     * 更新 - 根据Ids
     *
     * @param ids
     * @param orderItemDO
     */
    void updateByIds(
            @Param("ids") List<Integer> ids,
            @Param("orderItemDO") OrderItemDO orderItemDO
    );

    /**
     * 获取 - 根据 ids 查询
     *
     * @param ids
     * @return
     */
    List<OrderItemDO> selectByIds(@Param("ids") Collection<Integer> ids);

    /**
     * 查询 - 根据 orderIds 和 status
     *
     * @param orderIds
     * @param deleted
     * @return
     */
    List<OrderItemDO> selectByOrderIdsAndDeleted(
            @Param("orderIds") Collection<Integer> orderIds,
            @Param("deleted") Integer deleted
    );

    /**
     * 查询 - 根据 orderId 下的 item
     *
     * @param orderId
     * @return
     */
    List<OrderItemDO> selectByOrderIdAndDeleted(
            @Param("orderId") Integer orderId,
            @Param("deleted") @NotNull Integer deleted
    );
}
