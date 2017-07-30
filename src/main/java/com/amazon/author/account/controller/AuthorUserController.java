package com.amazon.author.account.controller;

import com.amazon.author.account.service.AuthorUserService;
import org.framework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by User on 2017/7/30.
 */
@Scope("prototype")
@Controller
@RequestMapping("/authorUserController")
public class AuthorUserController extends BaseController {

    @Autowired
    private AuthorUserService authorUserService;




}
