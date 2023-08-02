package cn.iocoder.mall.order.biz.service;

import cn.iocoder.common.framework.constant.DeletedStatusEnum;
import cn.iocoder.common.framework.util.ServiceExceptionUtil;
import cn.iocoder.common.framework.vo.CommonResult;
import cn.iocoder.mall.order.api.OrderLogisticsService;
import cn.iocoder.mall.order.api.OrderReturnService;
import cn.iocoder.mall.order.api.bo.OrderLastLogisticsInfoBO;
import cn.iocoder.mall.order.api.bo.OrderReturnInfoBO;
import cn.iocoder.mall.order.api.bo.OrderReturnListBO;
import cn.iocoder.mall.order.api.constant.OrderErrorCodeEnum;
import cn.iocoder.mall.order.api.constant.OrderReturnServiceTypeEnum;
import cn.iocoder.mall.order.api.constant.OrderReturnStatusEnum;
import cn.iocoder.mall.order.api.dto.OrderReturnApplyDTO;
import cn.iocoder.mall.order.api.dto.OrderReturnQueryDTO;
import cn.iocoder.mall.order.biz.convert.OrderReturnConvert;
import cn.iocoder.mall.order.biz.dao.OrderItemMapper;
import cn.iocoder.mall.order.biz.dao.OrderMapper;
import cn.iocoder.mall.order.biz.dao.OrderReturnMapper;
import cn.iocoder.mall.order.biz.dataobject.OrderDO;
import cn.iocoder.mall.order.biz.dataobject.OrderItemDO;
import cn.iocoder.mall.order.biz.dataobject.OrderReturnDO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 订单退货 service
 *
 * @author Sin
 * @time 2019-03-30 15:35
 */
@Service
@org.apache.dubbo.config.annotation.Service(validation = "true")
public class OrderReturnServiceImpl implements OrderReturnService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderReturnMapper orderReturnMapper;

    @Reference(validation = "true")
    private OrderLogisticsService orderLogisticsService;

    @Override
    public CommonResult orderReturnApply(OrderReturnApplyDTO orderReturnDTO) {
        OrderDO checkOrder = orderMapper.selectById(orderReturnDTO.getOrderId());

        // 检查订单是否 存在
        if (checkOrder == null) {
            return ServiceExceptionUtil.error(OrderErrorCodeEnum.ORDER_NOT_EXISTENT.getCode());
        }

        // 转换 DO
        OrderReturnDO orderReturnDO = OrderReturnConvert.INSTANCE.convert(orderReturnDTO);
        orderReturnDO
                .setOrderId(checkOrder.getId())
                // TODO: 2019-04-27 Sin 服务号生成规则
                .setServiceNumber(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16))
                .setOrderNo(checkOrder.getOrderNo())
                .setStatus(OrderReturnStatusEnum.RETURN_APPLICATION.getValue())
                .setCreateTime(new Date());

        // 保存申请信息
        orderReturnMapper.insert(orderReturnDO);
        return CommonResult.success(null);
    }

    @Override
    public String updateRefundSuccess(String orderId, Integer refundPrice) {
        return "success";
    }

    @Override
    public CommonResult<OrderReturnInfoBO> orderApplyInfo(Integer orderId) {

        // 检查订单是否退货
        OrderReturnDO orderReturnDO = orderReturnMapper.selectByOrderId(orderId);
        if (orderReturnDO == null) {
            return ServiceExceptionUtil.error(OrderErrorCodeEnum.ORDER_RETURN_NO_RETURN_APPLY.getCode());
        }

        List<OrderItemDO> orderItemDOList = orderItemMapper
                .selectByDeletedAndOrderId(DeletedStatusEnum.DELETED_NO.getValue(), orderId);

        // 订单不存在
        if (CollectionUtils.isEmpty(orderItemDOList)) {
            return ServiceExceptionUtil.error(OrderErrorCodeEnum.ORDER_NOT_EXISTENT.getCode());
        }

        // 转换 returnInfo
        OrderReturnInfoBO.ReturnInfo returnInfo = OrderReturnConvert.INSTANCE.convert(orderReturnDO);
        List<OrderReturnInfoBO.OrderItem> itemList = OrderReturnConvert.INSTANCE.convert(orderItemDOList);

        // 物流信息
        CommonResult<OrderLastLogisticsInfoBO> lastLogisticsCommonResult = orderLogisticsService
                .getLastLogisticsInfo(orderReturnDO.getOrderLogisticsId());

        if (lastLogisticsCommonResult.isError()) {
            return ServiceExceptionUtil.error(OrderErrorCodeEnum.ORDER_LOGISTICS_INVOKING_FAIL.getCode());
        }

        OrderLastLogisticsInfoBO lastLogisticsInfoBO = lastLogisticsCommonResult.getData();
        OrderReturnInfoBO orderReturnInfoBO = new OrderReturnInfoBO()
                .setOrderItems(itemList)
                .setReturnInfo(returnInfo)
                .setLastLogisticsInfo(lastLogisticsInfoBO);

        return CommonResult.success(orderReturnInfoBO);
    }

    @Override
    public CommonResult<OrderReturnListBO> orderReturnList(OrderReturnQueryDTO queryDTO) {
        int totalCount = orderReturnMapper.selectListCount(queryDTO);
        if (totalCount <= 0) {
            return CommonResult.success(
                    new OrderReturnListBO()
                            .setData(Collections.EMPTY_LIST)
                            .setIndex(queryDTO.getIndex())
                            .setPageSize(queryDTO.getPageSize())
                            .setTotalCount(0)
            );
        }
        List<OrderReturnDO> orderReturnDOList = orderReturnMapper.selectList(queryDTO);
        List<OrderReturnListBO.OrderReturn> orderReturnListBOList
                = OrderReturnConvert.INSTANCE.convertListBO(orderReturnDOList);

        return CommonResult.success(
                new OrderReturnListBO()
                        .setData(orderReturnListBOList)
                        .setIndex(queryDTO.getIndex())
                        .setPageSize(queryDTO.getPageSize())
                        .setTotalCount(totalCount)
        );
    }

    @Override
    public CommonResult orderReturnAgree(Integer id) {
        OrderReturnDO orderReturnDO = orderReturnMapper.selectById(id);
        if (orderReturnDO == null) {
            return ServiceExceptionUtil
                    .error(OrderErrorCodeEnum.ORDER_RETURN_NOT_EXISTENT.getCode());
        }

        // TODO: 2019/5/8 sin, 发送 MQ 消息，申请退货成功!
        // TODO: 2019/5/8 sin 退款：支付系统退款
        // TODO: 2019/5/8 sin 退货+退款：退回商品签收后，支付系统退款

        orderReturnMapper.updateByOrderId(
                new OrderReturnDO()
                        .setId(id)
                        .setStatus(OrderReturnStatusEnum.APPLICATION_SUCCESSFUL.getValue())
        );
        return CommonResult.success(null);
    }

    @Override
    public CommonResult orderReturnRefuse(Integer id) {
        OrderReturnDO orderReturnDO = orderReturnMapper.selectById(id);
        if (orderReturnDO == null) {
            return ServiceExceptionUtil.error(OrderErrorCodeEnum.ORDER_RETURN_NOT_EXISTENT.getCode());
        }

        orderReturnMapper.updateByOrderId(
                new OrderReturnDO()
                        .setId(id)
                        .setStatus(OrderReturnStatusEnum.APPLICATION_FAIL.getValue())
        );
        return CommonResult.success(null);
    }
}
