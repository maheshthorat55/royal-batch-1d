package com.skillup.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillup.entity.GameTableMgt;

public interface GameTableMgtRepo extends JpaRepository<GameTableMgt, Long>{

}
