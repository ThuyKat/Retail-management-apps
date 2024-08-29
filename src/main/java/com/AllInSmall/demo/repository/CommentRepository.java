package com.AllInSmall.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AllInSmall.demo.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

	List<Comment> findByOrderId(int id);

}
