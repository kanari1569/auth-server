package com.mpt.authservice.controller;

import java.io.IOException;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// import javax.servlet.ServletRequest;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mpt.authservice.dto.UserResponse;
import com.mpt.authservice.service.OauthService;
import com.mpt.authservice.SocialLogin.SocialLoginType;

@RestController
@RequestMapping("/api/oauth")
public class OauthController {
    @Autowired
    OauthService oauthService;

    @GetMapping("/login/{socialLoginType}")
    public void socialLogin(@PathVariable(name="socialLoginType") String SocialLoginPath,@RequestParam(name = "redirect_url", required = false) String redirect_url, HttpServletRequest  request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("redirect_url",redirect_url);
        
        SocialLoginType socialLoginType = SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        response.sendRedirect(oauthService.request(socialLoginType));
        // return ResponseEntity.ok().body(oauthService.request(socialLoginType));
    }

    @GetMapping("/login/{socialLoginType}/redirection")
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath, @RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state,HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("code======"+code);
        System.out.println("state=========="+state);
        SocialLoginType socialLoginType = SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        UserResponse userResponse = oauthService.oauthLogin(socialLoginType,code,state);
        
        HttpSession session = request.getSession();
        String redirect_url = (String)session.getAttribute("redirect_url");
        if(redirect_url != null){
            session.removeAttribute("redirect_url");
            response.sendRedirect(redirect_url+"?userResponse="+userResponse.toJson());
        }
    }

	// 파라미터로 전달받은 토큰 처리
    @GetMapping("/token")
    public ResponseEntity<UserResponse> validCheck(@RequestParam(name = "ACCESS_TOKEN", required = false) String token) throws IOException {
        UserResponse userResponse = oauthService.oauthVerifyToken(token);
        return ResponseEntity.ok().body(userResponse);
    }

    // 헤더로 전달받은 토큰 처리
    @GetMapping("/access_token_info")
    public ResponseEntity<UserResponse> getAccessTokenInfo(@RequestHeader HttpHeaders httpHeaders) throws IOException {
        String token = httpHeaders.getFirst("Authorization");
        if(token.toLowerCase().startsWith("Bearer".toLowerCase())) {
            token = token.substring("Bearer".length()).trim();
        }

        UserResponse userResponse = oauthService.oauthVerifyToken(token);
        // System.out.println(userResponse);
        return ResponseEntity.ok().body(userResponse);
    }
}
