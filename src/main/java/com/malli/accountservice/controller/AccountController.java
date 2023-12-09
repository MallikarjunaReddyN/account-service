package com.malli.accountservice.controller;

import com.malli.accountservice.domain.response.ApiResponse;
import com.malli.accountservice.domain.response.AccountStatusResponse;
import com.malli.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/account")
@Tag(name = "AccountController", description = "Account management APIs")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/status")
    public ApiResponse<AccountStatusResponse> showStatus() {
        AccountStatusResponse accountStatusResponse = new AccountStatusResponse("UP", "account-service", "Malli");
        return ApiResponse.success("200", ApiResponse.Status.SUCCESS.toString(), accountStatusResponse);
    }

}
