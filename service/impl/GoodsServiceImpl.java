package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.GoodsMapper;
import com.company.project.model.Goods;
import com.company.project.model.GoodsDetails;
import com.company.project.model.User;
import com.company.project.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class GoodsServiceImpl extends AbstractService<Goods> implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private UserService userService;

    @Resource
    private GoodsTypeService goodsTypeService;

    @Resource
    private OrderDetailsService orderDetailsService;

    @Resource
    private ShopService shopService;

    @Override
    public int findGoodsCount(Long goodsId) {
        return goodsMapper.findGoodsCount(goodsId);
    }

    @Override
    public List<Goods> selectByGoodsTypeId(Long goodsTypeId) {
        return goodsMapper.selectByGoodsTypeId(goodsTypeId);
    }

    @Override
    public Result detail(Long id, String userId) {
        Goods goods = goodsMapper.detail(id);
        if (null != goods){
            goods.setGoodsTypeName(goodsTypeService.getNameById(goods.getGoodsType()));
            if (null != goods.getCreatedBy()){
                List<User> userList = userService.findUserById(Long.valueOf(goods.getCreatedBy()));
                goods.setUserLikeDtoList(userList);
            }
            Random r = new Random(1);
            int number = r.nextInt(10);
            //增加曝光数
            goods.setExposureNumber(null == goods.getExposureNumber() ? 1 : goods.getExposureNumber() + number);
            //增加热度
            goods.setHeat(null == goods.getHeat() ? 1 : goods.getHeat() + 1);
            update(goods);
        }

        return ResultGenerator.genSuccessResult(goods);
    }

    @Override
    public Result list(String userId,Goods goods) {

        if (goods == null){
            goods = new Goods();
        }

        PageHelper.startPage(goods.getPage() == null ? 0 : goods.getPage(), goods.getLimit() == null ? 10 : goods.getLimit());
        goods.setIsDelete(false);
        List<Goods> list = goodsMapper.list(goods);
        if (list.size() > 0){
            for (Goods d:list) {
                d.setSalesVolume(orderDetailsService.getCountByGoodsId(d.getId()));
                d.setGoodsTypeName(goodsTypeService.getNameById(d.getGoodsType()));
                if (null != d.getCreatedBy()){
                    List<User> userList = userService.findUserById(Long.valueOf(d.getCreatedBy()));
                    d.setUserLikeDtoList(userList);
                }
                if (null != d.getShopId()){
                    d.setShopName(shopService.findByIdAndValidDelete(d.getShopId()).getName());
                }
            }
            list.sort(new Comparator<Goods>() {
                @Override
                public int compare(Goods o1, Goods o2) {
                    Integer i1 = o1.getSalesVolume();
                    Integer i2 = o2.getSalesVolume();
                    return i1.compareTo(i2);
                }
            });
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Override
    public Result add(Goods goods) {
        goods.setCreatedAt(new Date());
        goods.setIsDelete(false);
        save(goods);
        Result result= ResultGenerator.genSuccessResult();
        result.setData(goods);
        return result;
    }

    @Override
    public Result delete(Long id) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setIsDelete(true);
        update(goods);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result updateGoods(Goods goods) {
        goods.setUpdatedAt(new Date());
        update(goods);
        Result result= ResultGenerator.genSuccessResult();
        result.setData(goods);
        return result;
    }

    @Override
    public Result findByNumber(Goods goods) {
        PageHelper.startPage(goods.getPage(), goods.getLimit());
        goods.setIsDelete(false);
        List<Goods> list = goodsMapper.list(goods);
        Collections.shuffle(list);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Override
    public Result findByAllIds(Goods goods) {
        String[] array = goods.getIds().split(",");
        List<Goods> list = goodsMapper.findByAllIds(array);
        return ResultGenerator.genSuccessResult(list);
    }

    @Resource
    private GoodsDetailsService goodsDetailsService;

    @Override
    public Result recommendByUser(Goods goods) {
        List<Goods> list = recommend(goods.getCreatedBy());
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    //基于物品的协同过滤算法
    //通过计算余弦相似度并取TopN, 返回为uid的用户生成的5个推荐商品
    public List<Goods> recommend(String uid){

        List<GoodsDetails> goodsDetailsList = new ArrayList<GoodsDetails>();              //其他用户点击的商品列表

        List<User> users = userService.selectAllUsers();                                 //所有用户列表
        List<Goods> goodsList = goodsMapper.selectAllGoods();                            //所有商品列表
        int[][] curMatrix = new int[goodsList.size() + 1000][goodsList.size() + 1000];   //当前矩阵
        int[][] comMatrix = new int[goodsList.size() + 1000][goodsList.size() + 1000];   //共现矩阵
        int[] N = new int[goodsList.size() + 1000];                                      //点击每个商品的人数

        for(User user: users){
            if(user.getCreatedBy() == uid) {
                continue;                    //当前用户则跳过
            }

            goodsDetailsList = goodsDetailsService.findByUser(String.valueOf(user.getId())); //当前用户的点击商品列表

            for(int i = 0; i < goodsList.size(); i++) {
                for(int j = 0; j < goodsList.size(); j++) {
                    curMatrix[i][j] = 0;                               //清空矩阵
                }
            }

            for(int i = 0; i < goodsDetailsList.size(); i++){
                int pid1 = Math.toIntExact(goodsDetailsList.get(i).getGoodsId());
                ++N[pid1];
                for(int j = i+1; j < goodsDetailsList.size(); j++){
                    int pid2 = Math.toIntExact(goodsDetailsList.get(j).getGoodsId());
                    ++curMatrix[pid1][pid2];
                    ++curMatrix[pid2][pid1]; //两两加一
                }
            }
            //累加所有矩阵, 得到共现矩阵
            for(int i = 0; i < goodsList.size(); i++){
                for(int j = 0; j < goodsList.size(); j++){
                    int pid1 = goodsList.get(i).getId().intValue();
                    int pid2 = goodsList.get(j).getId().intValue();
                    comMatrix[pid1][pid2] += curMatrix[pid1][pid2];
                    comMatrix[pid1][pid2] += curMatrix[pid1][pid2];
                }
            }
        }

        TreeSet<Goods> preList = new TreeSet<Goods>(new Comparator<Goods>() {
            @Override
            public int compare(Goods o1, Goods o2) {
                if(o1.getSimilarity().equals(o2.getSimilarity())) {
                    return (int) (o1.getSimilarity().intValue()-o2.getSimilarity().intValue()) * 100;
                } else {
                    return o1.getHeat()-o2.getHeat();
                }
            }
        }); //预处理的列表

        goodsDetailsList = goodsDetailsService.findByUser(uid);           //当前用户的点击商品列表
        boolean[] used = new boolean[goodsList.size() + 1000];            //判重数组
        for(GoodsDetails d: goodsDetailsList){
            int Nij = 0;                                                    //既喜欢i又喜欢j的人数
            double Wij;                                                     //相似度
            Goods tmp;                                                      //当前的商品

            int i = d.getGoodsId().intValue();
            for(Goods goods: goodsList){
                if(d.getGoodsId().equals(goods.getId())) {
                    continue;
                }
                int j = goods.getId().intValue();

                Nij = comMatrix[i][j];
                Wij = (double)Nij/Math.sqrt(N[i] * N[j]); //计算余弦相似度

                tmp = findByIdAndValidDelete(goods.getId());
                if (Double.isNaN(Wij)){
                    tmp.setSimilarity(BigDecimal.valueOf(0));
                }else {
                    tmp.setSimilarity(BigDecimal.valueOf(Wij));
                }

                if(used[tmp.getId().intValue()]) {
                    continue;
                }
                preList.add(tmp);
                used[tmp.getId().intValue()] = true;
            }
        }

        List<Goods> recomLists = new ArrayList<Goods>();      //生成的推荐结果
        for(int i = 0; preList.size() > 0 && i<5; i++){
            recomLists.add(preList.pollLast());
            preList.pollLast();
        }
        if(recomLists.size() < 10){
            //推荐数量不满10个, 随机获取十个实例
            List<Goods> list = goodsMapper.randList();

            List<Goods> listAll = new ArrayList<Goods>();

            listAll.addAll(recomLists);
            listAll.addAll(list);

            listAll = new ArrayList<Goods>(new LinkedHashSet<>(listAll));

            recomLists = listAll;
        }

        return recomLists;
    }

    @Override
    public BigDecimal getGoodsCount() {
        return goodsMapper.getGoodsCount();
    }

    @Override
    public Result findAllByModal(Goods goods) {
        PageHelper.startPage(0, 0);
        goods.setIsDelete(false);
        List<Goods> list = goodsMapper.findAllByModal(goods);
        if (list.size() > 0){
            for (Goods d:list) {
                d.setSalesVolume(orderDetailsService.getCountByGoodsId(d.getId()));
                d.setGoodsTypeName(goodsTypeService.getNameById(d.getGoodsType()));
                if (null != d.getCreatedBy()){
                    List<User> userList = userService.findUserById(Long.valueOf(d.getCreatedBy()));
                    d.setUserLikeDtoList(userList);
                }
            }
            list.sort(new Comparator<Goods>() {
                @Override
                public int compare(Goods o1, Goods o2) {
                    Integer i1 = o1.getSalesVolume();
                    Integer i2 = o2.getSalesVolume();
                    //i2.compareTo(i1)倒叙
                    //i1.compareTo(i2)正序
                    return i2.compareTo(i1);
                }
            });
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
