package com.atlasinside.crondata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ApplicationDashboardLoaderService {
    private final ResourceLoader resourceLoader;

    @Autowired
    public ApplicationDashboardLoaderService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }

    public String pathResource(String location) throws IOException {
        try {
            String path = resourceLoader.getResource("classpath:" + location).getURL().getPath();
            if (path.startsWith("/")) {
                path = path.substring(1, path.length());
            }
            return path;
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
            return null;
        }

    }
}
