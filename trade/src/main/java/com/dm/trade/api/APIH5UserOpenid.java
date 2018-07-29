package com.dm.trade.api;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dm.trade.common.utils.HttpHelper;
import com.dm.trade.common.utils.JacksonHelper;
import com.dm.trade.common.utils.RequestHelper;
import com.dm.trade.common.utils.StringHelper;
import com.dm.trade.common.wx.ConfigUtil;

@RestController
@RequestMapping("/api/h5_openid")
public class APIH5UserOpenid {
	 private static final Logger logger = LoggerFactory.getLogger(APISystemUserController.class);

	    private static final String APPID = "wx16f8d77e7418393f";
	    private static final String APP_SECRET = "7e0a37d6c2afc722be56685024f3c31c";

	    /**
	     * 首次授权获取code，然后跳转获取openid
	     * @param request
	     * @param response
	     * @return
	     */
	    @RequestMapping(value = "/login1", method = {RequestMethod.GET},
	            produces = "text/html;charset=UTF-8",
	            consumes = MediaType.ALL_VALUE)
	    @ResponseBody
	    public String setup1(HttpServletRequest request, HttpServletResponse response) {

	        /**
	         * 组装跳转微信地址
	         */
	        String wxURL = "";
	        try {
	            wxURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect"
	                    .replace("APPID", APPID)
	                    .replace("REDIRECT_URI", URLEncoder.encode((RequestHelper.fullServletPath(request) + "/api/relation/h5list.cgi"), "UTF-8"))
	                    .replace("SCOPE", "snsapi_base")
	                    .replace("STATE", RequestHelper.sessionId(request).substring(0, 15));
	           /* HttpHelper.get(wxURL);*/
	        } catch (Exception e) {
	            logger.error("组装wxURL1异常", e);
	        }
	        /**
	         * 然后跳转呗
	         */
	        try {
	            logger.debug("[{}] 跳转微信授权页面 [{}]", RequestHelper.sessionId(request), wxURL);
	            response.sendRedirect(wxURL);
	            return null;
	        } catch (IOException e) {
	            e.printStackTrace();
	            logger.error("跳转失败2", e);
	            response.setStatus(500);
	            return null;
	        }
	    }

	    /**
	     * 获取用户openid
	     * @param request
	     * @param response
	     * @return
	     */
	    @RequestMapping(value = "/login2", method = {RequestMethod.GET},
	            produces = "text/html;charset=UTF-8",
	            consumes = MediaType.ALL_VALUE)
	    @ResponseBody
	    public String setup2(String code) {

	        /**
	         * 再判断是否合法的微信返回
	         */
	     
	        if (!StringHelper.checkString(code)) {
	            return "/oauth2/login1";
	        }
	       
	            String url = ConfigUtil.codeurl+
	                    "?appid="+ APPID+
	                    "&secret="+APP_SECRET+
	                    "&js_code="+code+
	                    "&grant_type="+ConfigUtil.grant_type;
	            String result = "";
	            try {
	                result = HttpHelper.get(url);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            return result;//----
	     
	    }
}
