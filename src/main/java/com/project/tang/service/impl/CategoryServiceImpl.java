package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.tang.dao.mapper.CategoryMapper;
import com.project.tang.dao.pojo.Category;
import com.project.tang.service.CategoryService;
import com.project.tang.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result getCategoryList() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(categories);
    }
}
