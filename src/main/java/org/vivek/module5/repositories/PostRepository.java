package org.vivek.module5.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vivek.module5.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
