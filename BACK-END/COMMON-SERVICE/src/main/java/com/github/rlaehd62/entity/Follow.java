package com.github.rlaehd62.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Follow
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long ID;
	@NonNull @ManyToOne(targetEntity = Account.class) private Account follower;
	@NonNull @ManyToOne(targetEntity = Account.class) private Account followee;
}
