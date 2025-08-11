package cn.iocoder.mall.systemservice.rpc.permission;

import cn.iocoder.common.framework.vo.CommonResult;
import cn.iocoder.mall.systemservice.manager.permission.ResourceManager;
import cn.iocoder.mall.systemservice.rpc.permission.dto.ResourceCreateDTO;
import cn.iocoder.mall.systemservice.rpc.permission.dto.ResourceUpdateDTO;
import cn.iocoder.mall.systemservice.rpc.permission.vo.ResourceVO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cn.iocoder.common.framework.vo.CommonResult.success;

/**
* 资源 Rpc 实现类
*/
@Service(version = "${dubbo.provider.ResourceRpc.version}", validation = "false")
public class ResourceRpcImpl implements ResourceRpc {

    @Autowired
    private ResourceManager resourceManager;

    @Override
    public CommonResult<Integer> createResource(ResourceCreateDTO createDTO) {
        return success(resourceManager.createResource(createDTO));
    }

    @Override
    public CommonResult<Boolean> updateResource(ResourceUpdateDTO updateDTO) {
        resourceManager.updateResource(updateDTO);
        return success(true);
    }

    @Override
    public CommonResult<Boolean> deleteResource(Integer resourceId) {
        resourceManager.deleteResource(resourceId);
        return success(true);
    }

    @Override
    public CommonResult<ResourceVO> getResource(Integer resourceId) {
        return success(resourceManager.getResource(resourceId));
    }

    @Override
    public CommonResult<List<ResourceVO>> listResource(List<Integer> resourceIds) {
        return success(resourceManager.listResource(resourceIds));
    }

}
