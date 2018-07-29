package com.dm.trade.api;

import com.dm.trade.api.dto.APIResult;
import com.dm.trade.api.dto.request.h5.GoodsListQueryOptionH5;
import com.dm.trade.api.dto.request.order.OrderAddForm;
import com.dm.trade.api.dto.response.order.OrderCreateResult;
import com.dm.trade.common.http.impl.UserApi;
import com.dm.trade.common.utils.HttpContextUtils;
import com.dm.trade.common.utils.JSONUtils;
import com.dm.trade.common.utils.StringUtils;
import com.dm.trade.customer.domain.CustomerDO;
import com.dm.trade.customer.service.CustomerService;
import com.dm.trade.goods.domain.GoodsCategoryDO;
import com.dm.trade.goods.domain.GoodsDO;
import com.dm.trade.goods.service.GoodsCategoryService;
import com.dm.trade.goods.service.GoodsService;
import com.dm.trade.order.service.OrderService;
import com.google.common.collect.Maps;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangle1
 * @title APIH5Controller.java
 * @Date 2018-07-15
 * @since v1.0.0
 */
@Controller
@RequestMapping(value = "/api/relation")
public class APIH5Controller {
	 private static final Logger logger = LoggerFactory.getLogger(APICustomerController.class);

	@Resource
	private CustomerService customerService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private APIH5UserOpenid h5UserOpenid;
   
    /**
     * 进入分类页面，获取商品类目，默认热销
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/h5list")
    public String h5list(Model model, HttpServletRequest request) throws Exception {
    	  String resp = null; 
    	 String code = request.getParameter("code");
    	
    	 resp = this.h5UserOpenid.setup2(code.toString());
         logger.info("获取微信用户授权信息=======》" + resp);
         Map<String, Object> map = JSONUtils.jsonToMap(resp);
         String openId = (String) map.get("openid");//获取用户openid
         logger.info("获取微信用户openid=======》" + openId);
         
         //获取用户信息
         Map<String, Object> resultMap = new HashMap<>();
         CustomerDO customer = loginByOpenid(openId, request);
         resultMap.put("sessionId", request.getSession().getId());
         model.addAttribute("sessionId",request.getSession().getId());
    	 model.addAttribute("openId", openId);
         //返回用户状态
         if (customer != null) {
             model.addAttribute("customerStatus", customer.getStatus());
             model.addAttribute("customer", customer);
         }else {
             //未注册
             try {
                 CustomerDO user = new CustomerDO();
                 if (openId != null) {
                     user.setOpenId(openId);
                     user.setCreateTime(new Timestamp(System.currentTimeMillis()));
                     customerService.save(user);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         
        Map<String, Object> dishTypeParams = Maps.newHashMap();
        dishTypeParams.put("sort", "id");
        dishTypeParams.put("order", "desc");
        List<GoodsCategoryDO> categoryDOS = goodsCategoryService.list(dishTypeParams);
        model.addAttribute("types", categoryDOS);

        return "wx/h5/dish";
    }

    /**
     * 获取商品列表
     * @param goodsListQueryOption
     * @return
     */
    @RequestMapping(value = "/goodsList")
    @ResponseBody
    public APIResult queryDishes(@Valid GoodsListQueryOptionH5 goodsListQueryOption) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(goodsListQueryOption.getName())) {
            map.put("name", goodsListQueryOption.getName());
        }
        if (StringUtils.isNotEmpty(goodsListQueryOption.getCategoryId()+"")) {
            map.put("categoryId", goodsListQueryOption.getCategoryId());
        }
        List<GoodsDO> dicdishesDOS = goodsService.list(map);
        return APIResult.isOk(dicdishesDOS);
    }

    /**
     * 跳转去支付页面
     * @return
     */
    @RequestMapping("/payDetail")
    public String dishesDetailList() {
        return "wx/h5/dishdetail";
    }

    /**
     * 创建订单
     *
     * @param form
     * @return
     */
    @PostMapping("create")
    public APIResult createOrder(@Valid OrderAddForm form, HttpServletRequest request) {
        CustomerDO customerInfo = HttpContextUtils.getCustomerInfo();
        OrderCreateResult orderAndValid = orderService.createOrderAndValid(form, customerInfo);
        return APIResult.isOk(orderAndValid);
    }
    /**
     * 根据openid登陆获取用户信息
     *
     * @param openid
     * @param request
     * @return
     */
    private CustomerDO loginByOpenid(String openid, HttpServletRequest request) {
        Assert.notNull(openid, "openid不能为空");
        Map<String, Object> map = Maps.newHashMap();
        map.put("openId", openid);
        List<CustomerDO> list = customerService.list(map);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 0) {
            CustomerDO customer = list.get(0);
            HttpContextUtils.setCustomerInfo(request, customer);
            return customer;
        }
        return null;
    }
}
