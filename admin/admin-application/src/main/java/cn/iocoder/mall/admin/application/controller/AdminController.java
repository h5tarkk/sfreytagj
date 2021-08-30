package cn.iocoder.mall.admin.application.controller;

import cn.iocoder.common.framework.vo.CommonResult;
import cn.iocoder.mall.admin.api.AdminService;
import cn.iocoder.mall.admin.api.ResourceService;
import cn.iocoder.mall.admin.api.bo.AdminPageBO;
import cn.iocoder.mall.admin.api.bo.ResourceBO;
import cn.iocoder.mall.admin.api.constant.ResourceConstants;
import cn.iocoder.mall.admin.api.dto.AdminPageDTO;
import cn.iocoder.mall.admin.application.convert.AdminConvert;
import cn.iocoder.mall.admin.application.convert.ResourceConvert;
import cn.iocoder.mall.admin.application.vo.AdminMenuTreeNodeVO;
import cn.iocoder.mall.admin.application.vo.AdminPageVO;
import cn.iocoder.mall.admin.sdk.context.AdminSecurityContextHolder;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/admin")
@Api("管理员模块")
public class AdminController {

    @Reference(validation = "true")
    private ResourceService resourceService;
    @Reference(validation = "true")
    private AdminService adminService;

    // =========== 当前管理员相关的资源 API ===========

    @SuppressWarnings("Duplicates")
    @GetMapping("/menu_resource_tree")
    @ApiOperation(value = "获得当前登陆的管理员拥有的菜单权限", notes = "以树结构返回")
    public CommonResult<List<AdminMenuTreeNodeVO>> menuResourceTree() {
        List<ResourceBO> resources = resourceService.getResourcesByTypeAndRoleIds(ResourceConstants.TYPE_MENU, AdminSecurityContextHolder.getContext().getRoleIds());
        // 创建 AdminMenuTreeNodeVO Map
        Map<Integer, AdminMenuTreeNodeVO> treeNodeMap = resources.stream().collect(Collectors.toMap(ResourceBO::getId, ResourceConvert.INSTANCE::convert));
        // 处理父子关系
        treeNodeMap.values().stream()
                .filter(node -> !node.getPid().equals(ResourceConstants.PID_ROOT))
                .forEach((childNode) -> {
                    // 获得父节点
                    AdminMenuTreeNodeVO parentNode = treeNodeMap.get(childNode.getPid());
                    if (parentNode.getChildren() == null) { // 初始化 children 数组
                        parentNode.setChildren(new ArrayList<>());
                    }
                    // 将自己添加到父节点中
                    parentNode.getChildren().add(childNode);
                });
        // 获得到所有的根节点
        List<AdminMenuTreeNodeVO> rootNodes = treeNodeMap.values().stream()
                .filter(node -> node.getPid().equals(ResourceConstants.PID_ROOT))
                .sorted(Comparator.comparing(AdminMenuTreeNodeVO::getSort))
                .collect(Collectors.toList());
        return CommonResult.success(rootNodes);
    }

    @GetMapping("/url_resource_list")
    @ApiOperation(value = "获得当前登陆的管理员拥有的 URL 权限列表")
//    @ApiModelProperty(value = "data", example = "['/admin/role/add', '/admin/role/update']") 没效果
    public CommonResult<Set<String>> urlResourceList() {
        List<ResourceBO> resources = resourceService.getResourcesByTypeAndRoleIds(ResourceConstants.TYPE_URL, AdminSecurityContextHolder.getContext().getRoleIds());
        return CommonResult.success(resources.stream().map(ResourceBO::getHandler).collect(Collectors.toSet()));
    }

    // =========== 管理员管理 API ===========

    @GetMapping("/page")
    @ApiOperation(value = "管理员分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "昵称，模糊匹配", required = true, example = "小王"),
            @ApiImplicitParam(name = "pageNo", value = "页码，从 0 开始", example = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, example = "10"),
    })
    public CommonResult<AdminPageVO> page(@RequestParam(value = "nickname", required = false) String nickname,
                                          @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        CommonResult<AdminPageBO> result = adminService.getAdminPage(new AdminPageDTO().setNickname(nickname).setPageNo(pageNo).setPageSize(pageSize));
        return AdminConvert.INSTANCE.convert(result);
    }

}