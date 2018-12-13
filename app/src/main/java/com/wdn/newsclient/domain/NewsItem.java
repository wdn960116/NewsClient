package com.wdn.newsclient.domain;

/**
 * 新闻信息实体
 */
public class NewsItem {
    //新闻标题
    private String title;
   //新闻描述
    private String desc;
    //图片路径
    private String imagePath;
    //新闻类型
    private String type;
    //新闻评论数
    private int commentCount;

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", type='" + type + '\'' +
                ", commentCount=" + commentCount +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
