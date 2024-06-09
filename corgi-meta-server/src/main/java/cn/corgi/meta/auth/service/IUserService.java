package cn.corgi.meta.auth.service;

import cn.corgi.meta.auth.bean.AuthingVO;

/**
 * @author wanbeila
 * @date 2024/6/9
 */
public interface IUserService {
    void fillUserInfo(AuthingVO authingVO);
}
