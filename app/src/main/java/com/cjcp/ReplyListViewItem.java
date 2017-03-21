package com.cjcp;

/**
 * Created by Pc on 2016-04-11.
 */
public class ReplyListViewItem {
    private String userName, signDate, wrContent, starRank, wrCommentReply;
    int screen;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getWrContent() {
        return wrContent;
    }

    public void setWrContent(String wrContent) {
        this.wrContent = wrContent;
    }

    public String getStarRank() {
        return starRank;
    }

    public void setStarRank(String starRank) {
        this.starRank = starRank;
    }

    public String getWrCommentReply() {
        return wrCommentReply;
    }

    public void setWrCommentReply(String wrCommentReply) {
        this.wrCommentReply = wrCommentReply;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }
}
