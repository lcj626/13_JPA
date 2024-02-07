package com.ohgiraffers.springjpa.category.service;

import com.ohgiraffers.springjpa.category.entity.Category;
import com.ohgiraffers.springjpa.category.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public Integer findByCategory(int categoryCode){ // findByCategory 조회
//        if((categoryCode < 0) ){
//            return null;
//        }

        Category category = categoryRepo.findByCategoryCode(categoryCode);

        if(Objects.isNull(category.getCategoryCode())){
            return null;
        }

        return category.getCategoryCode();

//        return categoryRepo.findByCategoryCode(categoryCode).getCategoryCode();
    }


    public Category insertCategory(String categoryName) {

        //이름 중복 여부
        Category foundCategory = categoryRepo.findByCategoryName(categoryName);
        if(!Objects.isNull(foundCategory)){
            return null;
        }

        //여기서부턴 서버문제
        Category insertCategory = new Category();
        insertCategory.setCategoryName(categoryName);

        Category result = categoryRepo.save(insertCategory);


        return result;

    }
}
