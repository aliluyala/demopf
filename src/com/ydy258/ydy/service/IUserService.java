package com.ydy258.ydy.service;

import java.util.List;

public interface IUserService extends  IBaseService {
	public List query(String userName,String password )throws Exception;
}
