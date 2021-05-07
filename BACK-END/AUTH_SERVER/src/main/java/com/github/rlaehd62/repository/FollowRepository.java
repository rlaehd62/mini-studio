package com.github.rlaehd62.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>
{
	public List<Follow> findByFollower_Id(String Follower_Id);
	public Optional<Follow> findByFollower_IdAndFollowee_Id(String Follower_Id, String Followee_Id);
}
