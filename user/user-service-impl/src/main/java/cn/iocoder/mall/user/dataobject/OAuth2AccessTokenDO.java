package cn.iocoder.mall.user.dataobject;

import java.util.Date;

public class OAuth2AccessTokenDO {

    /**
     * 访问令牌
     */
    private String id;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户编号
     */
    private Long uid;
    /**
     * 过期时间
     */
    private Date expiresTime;
    /**
     * 是否有效
     */
    private Boolean valid;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public OAuth2AccessTokenDO setId(String id) {
        this.id = id;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public OAuth2AccessTokenDO setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Long getUid() {
        return uid;
    }

    public OAuth2AccessTokenDO setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public OAuth2AccessTokenDO setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
        return this;
    }

    public Boolean getValid() {
        return valid;
    }

    public OAuth2AccessTokenDO setValid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OAuth2AccessTokenDO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}