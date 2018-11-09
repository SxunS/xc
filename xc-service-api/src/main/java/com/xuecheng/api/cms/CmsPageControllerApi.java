package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author s_xun
 */
@Api(value = "cms页面管理接口",description = "提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    /**
     * 页面查询
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码",paramType = "path",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数",paramType = "path",dataType = "int",required = true)
    })
    QueryResponseResult findList(int pageNo, int pageSize, QueryPageRequest queryPageRequest);

    /**
     * 添加页面
     */
    @ApiOperation("添加页面")
    CmsPageResult add(CmsPage cmsPage);

    /**
     * 删除页面
     * @param id 页面id
     * @return springData ResponseResult
     */
    @ApiOperation("删除页面")
    ResponseResult del(String id);

    /**
     * 根据id 查询页面
     * @param id 页面id
     * @return 页面模型
     */
    @ApiOperation("根据id 查询页面")
    CmsPage findById(String id);

    /**
     * 修改页面
     * @param id  页面id
     * @param cmsPage  修改后的页面
     * @return cmsPageResult
     */
    @ApiOperation("修改页面")
    CmsPageResult edit(String id, CmsPage cmsPage);
}
