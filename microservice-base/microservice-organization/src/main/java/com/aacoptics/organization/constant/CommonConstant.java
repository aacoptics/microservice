package com.aacoptics.organization.constant;

public interface CommonConstant {

    /**
     * 删除标志 1 未删除 0
     */
    Integer DEL_FLAG_1 = 1;

    Integer DEL_FLAG_0 = 0;

    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;

    Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 510
     */
    Integer SC_JEECG_NO_AUTHZ = 510;

    /**
     * 登录用户令牌缓存KEY前缀
     */
    long TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000; //24Hour  2019081510214

    String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN_";

    /**
     * 0：一级菜单
     */
    Integer MENU_TYPE_0 = 0;

    /**
     * 1：子菜单
     */
    Integer MENU_TYPE_1 = 1;

    /**
     * 2：按钮权限
     */
    Integer MENU_TYPE_2 = 2;

    /**
     * 是否用户已被冻结 1(解冻)正常 2冻结
     */
    Integer USER_UNFREEZE = 1;

    Integer USER_FREEZE = 2;

    /**
     * 流程状态 0进行中 1已完成 2已删除
     */
    Integer ON_PROCESSING = 0;

    Integer FINISH_PROCESSING = 1;

    Boolean DELETE_PROCESSING = true;

    // 过期时间
    long SECRET_EXPIRATION = 60 * 5;

    // 过期前1分钟
    long SECRET_LATS_TIME = 60;

    /**
     * token的key
     */
    String ACCESS_TOKEN = "Access-Token";

    /**
     * token的key
     */
    String USER_ID = "User-Id";

    /**
     * 接口标识
     */
    String SECRET_KEY = "secretKey";

    /**
     * 登录用户规则缓存
     */
    String LOGIN_USER_RULES_CACHE = "loginUser_cacheRules";

    /**
     * 登录用户拥有角色缓存KEY前缀
     */
    String LOGIN_USER_CACHE_RULES_ROLE = "loginUser_cacheRules::Roles_";

    /**
     * 登录用户拥有权限缓存KEY前缀
     */
    String LOGIN_USER_CACHE_RULES_PERMISSION = "loginUser_cacheRules::Permissions_";

    /**
     * 邮件发件人
     */
    String MAIL_FROM_S_LENS = "s-lens@aactechnologies.com";

}
