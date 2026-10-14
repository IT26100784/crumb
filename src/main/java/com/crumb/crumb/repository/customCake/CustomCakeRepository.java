package com.crumb.crumb.repository.customCake;

import com.crumb.crumb.model.customCake.CustomCakeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomCakeRepository extends JpaRepository<CustomCakeModel, Long> {
}