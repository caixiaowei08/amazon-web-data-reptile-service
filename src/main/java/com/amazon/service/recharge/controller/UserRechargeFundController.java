package com.amazon.service.recharge.controller;

import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.promot.price.service.PromotPriceService;
import com.amazon.service.recharge.entity.UserRechargeFundEntity;
import com.amazon.service.recharge.service.UserRechargeFundService;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userRechargeFundController")
public class UserRechargeFundController extends BaseController {

    @Autowired
    private UserRechargeFundService userRechargeFundService;

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            userRechargeFundEntity.setUpdateTime(new Date());
            userRechargeFundEntity.setCreateTime(new Date());
            userRechargeFundService.save(userRechargeFundEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    userRechargeFundEntity = userRechargeFundService.find(UserRechargeFundEntity.class,Integer.parseInt(id_array[i]));
                    userRechargeFundService.delete(userRechargeFundEntity);
                }
            }else{
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
    public AjaxJson doGet(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = userRechargeFundEntity.getId();
        if(id == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        UserRechargeFundEntity userRechargeFundEntityDb = userRechargeFundService.find(UserRechargeFundEntity.class, id);
        if(userRechargeFundEntityDb == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("数据不存在！");
            return j;
        }
        j.setContent(userRechargeFundEntityDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        UserRechargeFundEntity t = userRechargeFundService.find(UserRechargeFundEntity.class, userRechargeFundEntity.getId());
        try {
            userRechargeFundEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(userRechargeFundEntity, t);
            userRechargeFundService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }

}
