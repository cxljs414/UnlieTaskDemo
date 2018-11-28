package com.example.cx.unlietaskdemo.entity.net;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Msg {
    public static final String INFO = "info";
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String NEEDLOGIN = "needLogin";
    public static final String BADPARAMS = "badParams";
    private String flag;//消息标志
    private String content;//消息内容

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "flag='" + flag + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获得内容的实体对象
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getContentEntity(Class<T> tClass) {
        return JSON .parseObject(getContent(), tClass);
    }

    /**
     * 获得内容的实体对象数组
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> List<T> getContentEntityArray(Class<T> tClass) {
        return JSON.parseArray(getContent(), tClass);
    }
}
