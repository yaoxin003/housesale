package com.yx.housesale.biz.service.impl;

import com.google.common.base.Joiner;
import com.yx.housesale.biz.mapper.CommunityMapper;
import com.yx.housesale.biz.mapper.HouseMapper;
import com.yx.housesale.biz.mapper.HouseMsgMapper;
import com.yx.housesale.biz.mapper.HouseUserMapper;
import com.yx.housesale.biz.service.FileService;
import com.yx.housesale.biz.service.HouseService;
import com.yx.housesale.common.constant.HouseConstant;
import com.yx.housesale.common.constant.HouseUserType;
import com.yx.housesale.common.model.*;
import com.yx.housesale.common.page.PageData;
import com.yx.housesale.common.page.PageParam;
import com.yx.housesale.common.utils.BeanHelper;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/14/18:33
 */
@Service
@Log4j
public class HouseServiceImpl implements HouseService {

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseUserMapper houseUserMapper;

    @Autowired
    private HouseMsgMapper houseMsgMapper;

    @Autowired
    private FileService fileService;

    @Value("${file.prefix}")
    private String imgPrefix;

    @Override
    public PageData<House> getHouseList(House query, PageParam pageParam) {
        List<House> houseList = new ArrayList<>();
        this.buildCommunityQueryCondition(query);
        log.debug("【query=】" + query);
        houseList = this.getHouseListAndSetImg(query,pageParam);
        int count = houseMapper.selectPageCount(query);
        log.debug("【count=】" + count);
        PageData<House> housePageData =
                PageData.buildPage(houseList, count, pageParam.getPageSize(), pageParam.getPageNum());
        log.debug("【housePageData=】" + housePageData);
        return housePageData;
    }

    @Override
    public House getOneHouseById(Integer id) {
        House house = null;
        if(id != null){
            House paramHouse = new House();
            paramHouse.setId(id.longValue());
            house = houseMapper.selectOne(paramHouse);
            this.buildHouseImgPath(house);
        }
        log.debug("【house=】" + house);
        return house;
    }

    private void buildHouseImgPath(House house) {
        if(house != null){
            List<House> houses = new ArrayList<>();
            houses.add(house);
            this.buildHouseImgPath(houses);
        }
    }

    @Override
    public List<HouseUser> getSaleHouseUserList(Integer houseId) {
        HouseUser param = new HouseUser();
        param.setHouseId(houseId);
        param.setType(HouseConstant.DB_HOUSE_HOUSE_USER_TYPE_SALE);
        List<HouseUser> houseUsers = houseUserMapper.select(param);
        return houseUsers;
    }

    public Integer addHouseMsg(HouseMsg houseMsg){
        int insertCount = houseMsgMapper.insert(houseMsg);
        log.debug("【insertCount=】" + insertCount);
        return insertCount;
    }

    private List<House> getHouseListAndSetImg(House query, PageParam pageParam) {
        List<House> houseList = houseMapper.selectPageHouses(query, pageParam);
        this.buildHouseImgPath(houseList);
        return houseList;
    }

    private void buildHouseImgPath(List<House> houseList) {
        houseList.forEach(house->{
            house.setFirstImage(imgPrefix+ house.getFirstImage());
            house.setImageList(
                    house.getImageList().stream().map(img -> imgPrefix+img)
                            .collect(Collectors.toList()));

            house.setFloorPlanList(
                    house.getFloorPlanList().stream().map(img -> imgPrefix+img)
                            .collect(Collectors.toList()));
        });
        log.debug("【houseList=】" + houseList);
    }

    private void buildCommunityQueryCondition(House query) {
        String communityName = null;
        if(query!=null && StringUtils.isNotBlank((communityName=query.getCommunityName()))){
            List<Community> communities = this.getCommunitiesByName(communityName);
            if(!communities.isEmpty()){
                Community communityDB = communities.get(0);
                Integer communityId = communityDB.getId();
                if(communityId != null){
                    query.setCommunityId(communityId);
                }
            }
        }
        log.debug("【query=】" + query);
    }

    public List<Community> getAllCommunities(){
        Community community = new Community();
        List<Community> communities = this.queryCommunities(community);
        return communities;
    }

    /**
        * @description:
     * 1.解析房屋图片
     * 2.解析户型图片
     * 3.插入房产信息
     * 4.绑定用户到房产关系
        * @author:  YX
        * @date:    2020/01/22 13:04
        * @param: user
        * @param: house
        * @return: void
        * @throws:
        */
    @Override
    @Transactional
    public void addHouse(User user, House house) {
        if(!CollectionUtils.isEmpty(house.getFloorPlanFiles())){
            String floorPlanFiles = Joiner.on(",").join(fileService.getImgPath(house.getFloorPlanFiles()));
            log.debug("【floorPlanFiles=】" + floorPlanFiles);
            house.setFloorPlan(floorPlanFiles);
        }
        if(!CollectionUtils.isEmpty(house.getHouseFiles())){
            String images = Joiner.on(",").join(fileService.getImgPath(house.getHouseFiles()));
            log.debug("【images=】" + images);
            house.setImages(images);
        }
        this.buildProperties(house);
        int insertHouseCount = this.insertHouse(house);
        if(insertHouseCount == 1) {
            Long houseId = house.getId();
            log.debug("【houseId=】" + houseId);
            this.bindUser2House(houseId,user.getId(),true);
        }
    }

    public void bindUser2House(Long houseId, Long userId, boolean flag) {
        log.debug("【houseId=】" + houseId + "，【userId=】" + userId
                + "，【flag=】" + flag);
        HouseUser houseUser = this.buildHouseUser(houseId,
                userId,flag);
        this.insertHouseUser(houseUser);
    }


    public int unbindUser2House(Long houseId, Long userId, boolean flag){
        log.debug("【houseId=】" + houseId + "，【userId=】" + userId
                + "，【flag=】" + flag);
        HouseUser houseUser = this.buildHouseUser(houseId,
                userId,flag);
        return this.deleteHouseUser(houseUser);
    }


    private int deleteHouseUser(HouseUser houseUser){
        Example example = new Example(HouseUser.class);
        example.createCriteria().
            andEqualTo("userId",houseUser.getUserId()).
            andEqualTo("houseId",houseUser.getHouseId()).
            andEqualTo("type",houseUser.getType());
        int delCount = houseUserMapper.deleteByExample(example);
        log.debug("【delCount=】" + delCount);
        return delCount;
    }

    private void buildProperties(House house) {
        String[] propertiesArr = house.getPropertiesArr();
        log.debug("【propertiesArr=】" + propertiesArr);
        String properties = null;
        if(propertiesArr != null){
            properties = Joiner.on(",").join(propertiesArr);
            house.setProperties(properties);
        }
        log.debug("【properties=】" + properties);
    }

    private HouseUser buildHouseUser(Long houseId,Long userId,boolean isCollect){
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId.intValue());
        houseUser.setUserId(userId.intValue());
        houseUser.setType(isCollect ? HouseUserType.SALE.value : HouseUserType.BOOKMARK.value);
        return houseUser;
    }

    private int insertHouse(House house){
        BeanHelper.onInsert(house);
        BeanHelper.setDefaultProp(house,House.class);
        house.setId(null);
        int insertCount = houseMapper.insertSelective(house);
        log.debug("【insertCount=】" + insertCount + "【，house.getId()=】" + house.getId());
        return insertCount;
    }

    private int insertHouseUser(HouseUser houseUser){
        BeanHelper.setDefaultProp(houseUser,HouseUser.class);
        BeanHelper.onInsert(houseUser);
        int insertCount = houseUserMapper.insert(houseUser);
        log.debug("【insertCount=】" + insertCount);
        return insertCount;
    }

    private List<Community> getCommunitiesByName(String communityName) {
        Community param =this.buildCommunity(communityName);
        List<Community> communities = this.queryCommunities(param);
        return communities;
    }

    private Community buildCommunity(String communityName){
        Community community = new Community();
        community.setName(communityName);
        return community;
    }

    private List<Community> queryCommunities(Community param){
        List<Community> communities = communityMapper.select(param);
        log.debug("【communities=】" + communities);
        return communities;
    }

    public int updateRatingById(Long houseId,Double rating){
        House oneHouse = this.getOneHouseById(houseId.intValue());
        House house = new House();
        house.setRating(Math.min((oneHouse.getRating()+rating)/2 ,5d));

        Example example = new Example(House.class);
        example.createCriteria().andEqualTo("id",houseId);
        int i = houseMapper.updateByExampleSelective(house, example);
        log.debug("【i=】" + i);
        return i;
    }

}
