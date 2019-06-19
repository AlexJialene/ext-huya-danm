package com.ext.huya.dto;

public class MessageData {
    private String badgeName;
    private String content;
    private Integer fansLevel;
    private Integer nobleLevel;
    private Integer roomId;
    private String sendNick;
    private String senderAvatarUrl;
    private Integer senderGender;
    private Integer showMode;
    private String unionId;

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFansLevel() {
        return fansLevel;
    }

    public void setFansLevel(Integer fansLevel) {
        this.fansLevel = fansLevel;
    }

    public Integer getNobleLevel() {
        return nobleLevel;
    }

    public void setNobleLevel(Integer nobleLevel) {
        this.nobleLevel = nobleLevel;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getSendNick() {
        return sendNick;
    }

    public void setSendNick(String sendNick) {
        this.sendNick = sendNick;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    public Integer getSenderGender() {
        return senderGender;
    }

    public void setSenderGender(Integer senderGender) {
        this.senderGender = senderGender;
    }

    public Integer getShowMode() {
        return showMode;
    }

    public void setShowMode(Integer showMode) {
        this.showMode = showMode;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
