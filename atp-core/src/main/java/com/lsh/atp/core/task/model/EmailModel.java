package com.lsh.atp.core.task.model;


/**
 * Created by zhangqiang on 16/12/13.
 */
public class EmailModel {

    private String content;

    private String title;

    private String[] sendTo;



    public EmailModel(String content, String title){
        this(content,title,new String[]{"aop@lsh123.com"});
    }

    public EmailModel(String content, String title, String[] sendTo){
        this.content = content;
        this.sendTo = sendTo;
        this.title = title;
    }

    public String[] getSendTo() {
        return sendTo;
    }

    public void setSendTo(String[] sendTo) {
        this.sendTo = sendTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
