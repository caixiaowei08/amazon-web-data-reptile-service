package com.amazon.service.fund.controller;

import com.alipay.api.domain.AlipayTradePayModel;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.system.alipay.service.AlipayService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userFundController")
public class UserFundController extends BaseController {

    private static Logger logger = LogManager.getLogger(UserFundController.class.getName());

    @Autowired
    private UserFundService userFundService;

    @Autowired
    private AlipayService alipayService;


    @RequestMapping(params = "doAlipayTradePagePay")
    public void doAlipayTradePagePay(HttpServletRequest request, HttpServletResponse response) {
        AlipayTradePayModel alipayTradePayModel = new AlipayTradePayModel();
        alipayTradePayModel.setOutTradeNo("20170626010101001");
        alipayTradePayModel.setTotalAmount("0.01");
        alipayTradePayModel.setSubject("蔡晓伟测试01");
        alipayTradePayModel.setBody("蔡晓伟测试01 支付0.01元");
        alipayTradePayModel.setProductCode("FAST_INSTANT_TRADE_PAY");
        try {
             alipayService.doAlipayTradePayRequestPost(alipayTradePayModel, request, response);
        } catch (IOException ie) {
            logger.error(ie);
        }catch (ServletException se){
            logger.error(se);
        }
    }


    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(UserFundEntity userFundEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            userFundEntity.setUpdateTime(new Date());
            userFundEntity.setCreateTime(new Date());
            userFundService.save(userFundEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(UserFundEntity userFundEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    userFundEntity = userFundService.find(UserFundEntity.class, Integer.parseInt(id_array[i]));
                    userFundService.delete(userFundEntity);
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("请选择需要删除的数据！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
        }
        return j;
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(UserFundEntity userFundEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = userFundEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        UserFundEntity userFundDb = userFundService.find(UserFundEntity.class, id);
        if (userFundDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该数据不存在！");
            return j;
        }
        j.setContent(userFundDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(UserFundEntity userFundEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        UserFundEntity t = userFundService.find(UserFundEntity.class, userFundEntity.getId());
        try {
            userFundEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(userFundEntity, t);
            userFundService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }
}
