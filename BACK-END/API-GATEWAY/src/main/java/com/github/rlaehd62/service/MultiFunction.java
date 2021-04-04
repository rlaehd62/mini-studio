package com.github.rlaehd62.service;

@FunctionalInterface
public interface MultiFunction <T1, T2, R>
{
	public R apply(T1 t1, T2 t2);
}
