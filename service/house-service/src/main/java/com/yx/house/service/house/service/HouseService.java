package com.yx.house.service.house.service;

import com.yx.house.service.house.common.BeanHelper;
import com.yx.house.service.house.common.LimitOffset;
import com.yx.house.service.house.dao.UserDao;
import com.yx.house.service.house.mapper.CommunityMapper;
import com.yx.house.service.house.mapper.HouseMapper;
import com.yx.house.service.house.mapper.HouseMsgMapper;
import com.yx.house.service.house.mapper.HouseUserMapper;
import com.yx.house.service.house.model.*;
import com.yx.house.service.house.vo.HouseUserType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/28/8:42
 */
@Service
public class HouseService {

    Logger logger = LoggerFactory.getLogger(HouseService.class);

    @Value("${file.prefix}")
    private String imgPrefix;

    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private HouseUserMapper houseUserMapper;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMsgMapper houseMsgMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MailService mailService;


    /**
        * @description:
        * 1.添加房产
        * 2.绑定房产到用户关系
        * @author:  YX
        * @date:    2020/02/28 17:48
        * @param: house
        * @param: userId
        * @return: void
        * @throws:
        */
    @Transactional(rollbackFor = Exception.class)
    public void addHouse(House house, Long userId) {
        BeanHelper.setDefaultProp(house,House.class);
        BeanHelper.onInsert(house);
        houseMapper.insert(house);
        bindUser2House(house.getId(),userId, HouseUserType.SALE);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindUser2House(Long houseId,Long userId,HouseUserType sale){
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);
        houseUser.setUserId(userId);
        houseUser.setType(sale.value);
        List<HouseUser> houseUserList = houseUserMapper.select(houseUser);
        logger.info("【houseUserList=】"+ houseUserList);
        if(houseUserList != null && houseUserList.size()>0){
            return ;
        }
        BeanHelper.setDefaultProp(houseUser,HouseUser.class);
        BeanHelper.onInsert(houseUser);
        houseUserMapper.insert(houseUser);
        logger.info("insert finish");
    }

    public void unbindUser2House(Long houseId, Long userId, HouseUserType houseUserType) {
        Example example = new Example(HouseUser.class);
        example.createCriteria()
                .andEqualTo("houseId",houseId)
                .andEqualTo("userId",userId)
                .andEqualTo("type",houseUserType);
        houseUserMapper.deleteByExample(example);
    }

    public Pair<List<House>,Long> queryHouse(House query, LimitOffset build) {
        List<House> houses = new ArrayList<>();
        House houseQuery = query;
       /* if(StringUtils.isNoneBlank(query.getName())){
            List<Community> communityList = this.selectCommunityList(query.getName());
            if(!communityList.isEmpty()){
                houseQuery = new House();
                houseQuery.setCommunityId(communityList.get(0).getId());
            }
        }*/
        houses = this.queryAndSetImg(houseQuery,build);
        Long count = houseMapper.selectHouseCount(houseQuery);
        return ImmutablePair.of(houses,count);
    }

    public List<House> queryAndSetImg(House houseQuery, LimitOffset pageParams) {
        List<House> houses = houseMapper.selectHouse(houseQuery,pageParams);
        houses.forEach( h->{
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(
                    img -> imgPrefix + img
            ).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(
                    img -> imgPrefix + img
            ).collect(Collectors.toList()));
        });
        return houses;
    }

    private List<Community> selectCommunityList(String communitName) {
        Community community = new Community();
        community.setName(communitName);
        List<Community> list = communityMapper.select(community);
        return list;
    }

    public House queryOneHouse(long id){
        House query = new House();
        query.setId(id);
        List<House> houses = this.queryAndSetImg(query, LimitOffset.build(1, 0));
        if(!houses.isEmpty()){
            return houses.get(0);
        }
        return null;
    }

    public void addHouseMsg(HouseMsg houseMsg) {
        BeanHelper.onInsert(houseMsg);
        BeanHelper.setDefaultProp(houseMsg,HouseMsg.class);
        houseMsgMapper.insert(houseMsg);
        User user = userDao.getAgentDetail(houseMsg.getUserId());
        mailService.sendMail("来自用户"+houseMsg.getEmail(),
                houseMsg.getMsg(),user.getEmail());
    }

    public void updateRating(Long houseId, Double rating) {
        House house = this.queryOneHouse(houseId);
        Double oldRating = house.getRating();
        Double newRating = oldRating.equals(0d) ? rating:
                Math.min(Math.round(oldRating+rating)/2,5);

        House updateHouse = new House();
        updateHouse.setRating(newRating);
        Example example = new Example(House.class);
        example.createCriteria().andEqualTo("id",houseId);
        houseMapper.updateByExampleSelective(updateHouse,example);
    }
}
