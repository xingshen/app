package com.steptowin.albums;

/**
 * desc:单张照片实体
 * author：zg
 * date:16/6/13
 * time:下午1:43
 */
public class Picture {

    /**
     * 原图路径
     */
    private String path;
    /**
     * 缩略图路径
     */
    private String thumbPath;
    /**
     * 选择状态下,该照片是否被选中
     */
    private boolean checked;
    /**
     * 所属文件夹
     */
    private String folder;

    private int degree;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
