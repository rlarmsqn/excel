package com.jbt.water.vo;

import lombok.Data;

import java.util.List;

@Data
public class PushVO {
    private String kind;
    private String id;
    private String selfLink;
    private List<ChangeItem> changes;

    public static class ChangeItem {
        private String kind;
        private String type;
        private String fileId;
        private String time;
        private boolean removed;
    }
}
