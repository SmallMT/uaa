package com.easted.sy.user.archieves.uaa;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/")
public class UserEndpoint
{
    @RequestMapping(value = "principal")
    public Principal principal(Principal principal)
    {
//        Map<String, String> user = new HashMap<>();
//        user.put("email", principal.getName());
//        user.put("picture", "/pictures/".concat(principal.getName().concat("/avatar.png")));
//
//        return ResponseEntity.ok(user);
        return  principal;
    }

    @RequestMapping(value = "test")
    public String tEST()
    {
//        Map<String, String> user = new HashMap<>();
//        user.put("email", principal.getName());
//        user.put("picture", "/pictures/".concat(principal.getName().concat("/avatar.png")));
//
//        return ResponseEntity.ok(user);
        return  "test";
    }

}

