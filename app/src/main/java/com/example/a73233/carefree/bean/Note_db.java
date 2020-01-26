package com.example.a73233.carefree.bean;

import org.litepal.crud.LitePalSupport;

public class Note_db extends LitePalSupport {
    private int type;  //贴纸类型：记录贴为0、任务贴为1
    private String text; //墙贴文本
    private int rank;//强帖等级 记录贴等级为0，任务贴三个等级：日常(1)、一般(2)、紧急(3)
    private int isDone;//是否完成0和1表示
    private int idAbandon;//是否丢进垃圾桶0和1表示

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public int getIdAbandon() {
        return idAbandon;
    }

    public void setIdAbandon(int idAbandon) {
        this.idAbandon = idAbandon;
    }
}
