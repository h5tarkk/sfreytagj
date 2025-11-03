package cn.iocoder.mall.productservice.service.category.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
* 商品分类创建 BO
*/
@Data
@Accessors(chain = true)
public class ProductCategoryCreateBO {

    /**
     * 父分类编号
     */
    @NotNull(message = "父分类编号不能为空")
    private Integer pid;
    /**
     * 分类名称
     */
    @NotEmpty(message = "分类名称不能为空")
    private String name;
    /**
     * 分类描述
     */
    private String description;
    /**
     * 分类图片
     */
    private String picUrl;
    /**
     * 分类排序
     */
    @NotNull(message = "分类排序不能为空")
    private Integer sort;
    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

}
