package com.amazon.service.promot.order.controller;

import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.system.Constant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.RegularExpressionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tinygroup.tinyspider.Spider;
import org.tinygroup.tinyspider.Watcher;
import org.tinygroup.tinyspider.impl.SpiderImpl;
import org.tinygroup.tinyspider.impl.WatcherImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/promotOrderController")
public class PromotOrderController extends BaseController {

    @Autowired
    private PromotOrderService promotOrderService;

    @RequestMapping(params = "goNewPromot")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "pages/promot/newPromot";
    }

    @RequestMapping(params = "doDealAsinOrUrl")
    @ResponseBody
    public AjaxJson doDealAsinOrUrl(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String asinOrUrl = request.getParameter("asinOrUrl");
        if (!StringUtils.hasText(asinOrUrl)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入ASIN或者亚马逊商品主页链接！");
            return j;
        }
        String asin = "";
        String url = "";
        if (RegularExpressionUtils.isHttpOrHttps(asinOrUrl)) {
            int start = asinOrUrl.indexOf("/dp/") + 4;
            int end = asinOrUrl.indexOf("/ref=");
            asin = asinOrUrl.substring(start, end);
            url = asinOrUrl;
        } else {
            asin = asinOrUrl;
            String baseUrl = Constant.AMAZON_URL_PRODUCT;
            url = baseUrl.replace("#", asinOrUrl);
        }
        return j;
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            promotOrderEntity.setUpdateTime(new Date());
            promotOrderEntity.setCreateTime(new Date());
            promotOrderService.save(promotOrderEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PromotOrderEntity promotOrderEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    promotOrderEntity = promotOrderService.find(PromotOrderEntity.class, Integer.parseInt(id_array[i]));
                    promotOrderService.delete(promotOrderEntity);
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
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = promotOrderEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        PromotOrderEntity promotOrderEntityDb = promotOrderService.find(PromotOrderEntity.class, id);
        if (promotOrderEntityDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("数据不存在！");
            return j;
        }
        j.setContent(promotOrderEntityDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PromotOrderEntity promotOrderEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        PromotOrderEntity t = promotOrderService.find(PromotOrderEntity.class, promotOrderEntity.getId());
        try {
            promotOrderEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(promotOrderEntity, t);
            promotOrderService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }
}
