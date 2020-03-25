package com.yx.housesale.common.model;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
    * @description: setImages会特殊处理
    * @author:  YX
    * @date:    2020/01/14 18:29
    * @param: null
    * @return: 
    * @throws: 
    */
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer type;//1-销售,2-出租
    private Integer price;
    private String images;
    private Integer area;
    private Integer beds;
    private Integer baths;
    private Double rating;
    private String remarks;
    private String properties;
    private String floorPlan;
    private String tags;
    private Date createTime;
    private Date updateTime;
    private Integer cityId;
    private Integer communityId;
    private String address;
    private Integer state;

    @Transient
    private String firstImage;
    @Transient
    private List<String> imageList = new ArrayList<>();
    @Transient
    private List<String> floorPlanList = new ArrayList<>();
    @Transient
    private List<MultipartFile> houseFiles;
    @Transient
    private List<MultipartFile> floorPlanFiles;
    @Transient
    private Long userId;
    @Transient
    private boolean bookmarked;
    @Transient
    private String[] propertiesArr;

    @Transient
    private List<Long> ids;
    @Transient
    private String communityName;
    @Transient
    private String sort = "time_desc";
    @Transient
    private boolean joinHouseUser;
    @Transient
    private Integer houseUserType;//HouseUser.Type字段1-售卖，2-收藏

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
        if(!Strings.isNullOrEmpty(images)){
            List<String> list = Splitter.on(",").splitToList(images);
            if(!list.isEmpty()){
                this.firstImage = list.get(0);
                this.imageList = list;
            }
        }
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getBaths() {
        return baths;
    }

    public void setBaths(Integer baths) {
        this.baths = baths;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getFloorPlanList() {
        return floorPlanList;
    }

    public void setFloorPlanList(List<String> floorPlanList) {
        this.floorPlanList = floorPlanList;
    }

    public List<MultipartFile> getHouseFiles() {
        return houseFiles;
    }

    public void setHouseFiles(List<MultipartFile> houseFiles) {
        this.houseFiles = houseFiles;
    }

    public List<MultipartFile> getFloorPlanFiles() {
        return floorPlanFiles;
    }

    public void setFloorPlanFiles(List<MultipartFile> floorPlanFiles) {
        this.floorPlanFiles = floorPlanFiles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public boolean isJoinHouseUser() {
        return joinHouseUser;
    }

    public void setJoinHouseUser(boolean joinHouseUser) {
        this.joinHouseUser = joinHouseUser;
    }

    public String[] getPropertiesArr() {
        return propertiesArr;
    }

    public void setPropertiesArr(String[] propertiesArr) {
        this.propertiesArr = propertiesArr;
    }

    public Integer getHouseUserType() {
        return houseUserType;
    }

    public void setHouseUserType(Integer houseUserType) {
        this.houseUserType = houseUserType;
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", images='" + images + '\'' +
                ", area=" + area +
                ", beds=" + beds +
                ", baths=" + baths +
                ", rating=" + rating +
                ", remarks='" + remarks + '\'' +
                ", properties='" + properties + '\'' +
                ", floorPlan='" + floorPlan + '\'' +
                ", tags='" + tags + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", cityId=" + cityId +
                ", communityId=" + communityId +
                ", address='" + address + '\'' +
                ", state=" + state +
                ", firstImage='" + firstImage + '\'' +
                ", imageList=" + imageList +
                ", floorPlanList=" + floorPlanList +
                ", houseFiles=" + houseFiles +
                ", floorPlanFiles=" + floorPlanFiles +
                ", userId=" + userId +
                ", bookmarked=" + bookmarked +
                ", propertiesArr=" + Arrays.toString(propertiesArr) +
                ", ids=" + ids +
                ", communityName='" + communityName + '\'' +
                ", sort='" + sort + '\'' +
                ", joinHouseUser=" + joinHouseUser +
                ", houseUserType=" + houseUserType +
                '}';
    }
}