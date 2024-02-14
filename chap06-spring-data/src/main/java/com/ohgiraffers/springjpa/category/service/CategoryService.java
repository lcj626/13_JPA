package com.ohgiraffers.springjpa.category.service;

import com.ohgiraffers.springjpa.category.entity.Category;
import com.ohgiraffers.springjpa.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Integer findByCategory(int categoryCode){

        /*if ((categoryCode < 0)){
            return null;   없으면 어차피 null
        }*/
        /*        Category category = categoryRepository.findByCategoryCode(categoryCode);*/

        Category category = categoryRepository.findByCategoryCode(categoryCode);
        // 주어진 categoryCode에 해당하는 카테고리를 데이터베이스에서 찾는다


        if (Objects.isNull(category.getCategoryCode())){
            return null;
        } // categoryCode 가 null이면 null 반환하고


        return category.getCategoryCode(); // 아니면 categoryCode 반환
    }


    public Category insertCategory(String categoryName) {

        // 이름 중복 여부
        Category foundCategory = categoryRepository.findByCategoryName(categoryName);

        if (!Objects.isNull(foundCategory)){
            return null;
        }

        Category insertCategory = new Category();
        insertCategory.setCategoryName(categoryName);

        Category result = categoryRepository.save(insertCategory);

       /* if(result){

        }*/
        return result;

    }
}
