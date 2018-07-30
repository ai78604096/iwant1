package com.dm.trade.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dm.trade.api.weixin.mp.aes.AesException;
import com.dm.trade.api.weixin.mp.aes.WXPublicUtils;

@RestController
@RequestMapping("/api/wxpublic")
public class wxpublic {

	@RequestMapping("/verify_wx_token")
 @ResponseBody
    public String verifyWXToken(HttpServletRequest request) throws AesException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (WXPublicUtils.verifyUrl(msgSignature, msgTimestamp, msgNonce,echostr)) {
            return echostr;
        }
        return null;
    }
}
