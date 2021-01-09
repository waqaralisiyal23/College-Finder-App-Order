package models;

import java.io.Serializable;
import java.util.List;

public class Institute implements Serializable {
    String id;
    private String imageUrl;
    private String name;
    private String address;
    private String type;
    private List<String> departmentList;
    private String url;

    public Institute(){}

    public Institute(String id, String imageUrl, String name, String address, String type, List<String> departmentList, String url) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.address = address;
        this.type = type;
        this.departmentList = departmentList;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<String> departmentList) {
        this.departmentList = departmentList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
