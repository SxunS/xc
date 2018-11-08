package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
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
    public CmsPageResult add(CmsPage cmsPage);
}
