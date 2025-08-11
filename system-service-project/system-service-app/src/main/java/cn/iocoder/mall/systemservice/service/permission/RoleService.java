package cn.iocoder.mall.systemservice.service.permission;

import cn.iocoder.common.framework.util.ServiceExceptionUtil;
import cn.iocoder.common.framework.vo.PageResult;
import cn.iocoder.mall.systemservice.convert.permission.RoleConvert;
import cn.iocoder.mall.systemservice.dal.mysql.dataobject.permission.RoleDO;
import cn.iocoder.mall.systemservice.dal.mysql.mapper.permission.RoleMapper;
import cn.iocoder.mall.systemservice.enums.permission.RoleTypeEnum;
import cn.iocoder.mall.systemservice.service.permission.bo.RoleBO;
import cn.iocoder.mall.systemservice.service.permission.bo.RoleCreateBO;
import cn.iocoder.mall.systemservice.service.permission.bo.RolePageBO;
import cn.iocoder.mall.systemservice.service.permission.bo.RoleUpdateBO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.mall.systemservice.enums.SystemErrorCodeEnum.*;

/**
* 角色 Service
*/
@Service
@Validated
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
    * 创建角色
    *
    * @param createBO 创建角色 BO
    * @return 角色
    */
    public RoleBO createRole(@Valid RoleCreateBO createBO) {
        // 校验角色
        checkDuplicateRole(createBO.getName(), createBO.getCode(), null);
        // 插入到数据库
        RoleDO roleDO = RoleConvert.INSTANCE.convert(createBO);
        roleMapper.insert(roleDO);
        // 返回
        return RoleConvert.INSTANCE.convert(roleDO);
    }

    /**
    * 更新角色
    *
    * @param updateBO 更新角色 BO
    */
    public void updateRole(@Valid RoleUpdateBO updateBO) {
        // 校验更新的角色是否存在
        RoleDO roleDO = roleMapper.selectById(updateBO.getId());
        if (roleMapper.selectById(updateBO.getId()) == null) {
            throw ServiceExceptionUtil.exception(ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许修改
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getType())) {
            throw ServiceExceptionUtil.exception(ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
        // 校验角色的唯一字段是否重复
        checkDuplicateRole(updateBO.getName(), updateBO.getCode(), updateBO.getId());
        // 更新到数据库
        RoleDO updateObject = RoleConvert.INSTANCE.convert(updateBO);
        roleMapper.updateById(updateObject);
    }

    /**
    * 删除角色
    *
    * @param roleId 角色编号
    */
    public void deleteRole(Integer roleId) {
        // 校验删除的角色是否存在
        RoleDO roleDO = roleMapper.selectById(roleId);
        if (roleMapper.selectById(roleId) == null) {
            throw ServiceExceptionUtil.exception(ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getType())) {
            throw ServiceExceptionUtil.exception(ROLE_CAN_NOT_DELETE_SYSTEM_TYPE_ROLE);
        }
        // 标记删除
        roleMapper.deleteById(roleId);
//        // 发布角色删除事件，方便清理关联表 TODO 芋艿，需要实现
//        eventPublisher.publishEvent(new ResourceDeleteEvent(this, roleDeleteDTO.getId()));
    }

    /**
    * 获得角色
    *
    * @param roleId 角色编号
    * @return 角色
    */
    public RoleBO getRole(Integer roleId) {
        RoleDO roleDO = roleMapper.selectById(roleId);
        return RoleConvert.INSTANCE.convert(roleDO);
    }

    /**
    * 获得角色列表
    *
    * @param roleIds 角色编号列表
    * @return 角色列表
    */
    public List<RoleBO> listRole(List<Integer> roleIds) {
        List<RoleDO> roleDOs = roleMapper.selectBatchIds(roleIds);
        return RoleConvert.INSTANCE.convertList(roleDOs);
    }

    /**
     * 获得角色分页
     *
     * @param pageBO 角色分页查询
     * @return 角色分页结果
     */
    public PageResult<RoleBO> pageRole(RolePageBO pageBO) {
        IPage<RoleDO> roleDOPage = roleMapper.selectPage(pageBO);
        return RoleConvert.INSTANCE.convertPage(roleDOPage);
    }

    /**
     * 校验角色的唯一字段是否重复
     *
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
     */
    private void checkDuplicateRole(String name, String code, Integer id) {
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ROLE_CODE_DUPLICATE, name);
        }
    }

}
