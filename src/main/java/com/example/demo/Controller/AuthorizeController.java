package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Provider.GithubProvider;
import com.example.demo.Service.UserService;
import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserService userService;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.GetAccesstoken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null&&githubUser.getId()!=null){
            User user = new User();
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setBio(githubUser.getBio());
//            request.getSession().setAttribute("user",user);
            userService.cerateOrupdata(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        } else {
            log.error("callback get github error,{}", githubUser);
            return "redirect:/";

        }
    }
@GetMapping("/logout")
    public  String logout(HttpServletRequest request,
                          HttpServletResponse response){
                    request.getSession().removeAttribute("user");
                    Cookie cookie=new Cookie("token",null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    return "redirect:/";
}
}
