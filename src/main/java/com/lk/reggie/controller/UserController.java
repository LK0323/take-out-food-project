package com.lk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lk.reggie.common.R;
import com.lk.reggie.eneity.User;
import com.lk.reggie.service.UserService;
import com.lk.reggie.utils.SMSUtils;
import com.lk.reggie.utils.ValidateCodeUtils;
import com.sun.xml.internal.ws.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //调用API发送短信
            //SMSUtils.sendMessage("","",phone,code);
            //需要将生成的验证码保存到Session
            session.setAttribute(phone, code);

            return R.success("验证码发送成功");
        }

        return R.error("验证码发送失败");

    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //比对成功——>登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断当前手机号是否为新用户，若为新用户则自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");

    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession session){

        // 去除Session即可
        session.removeAttribute("user");

        return R.success("退出成功");
    }
}
