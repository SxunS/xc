package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsConfigService {

    @Autowired
    private CmsConfigRepository configRepository;

    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> cmsConfig = configRepository.findById(id);
        return cmsConfig.orElse(null);
    }
}
