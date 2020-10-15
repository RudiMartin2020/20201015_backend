package rms.backend.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @RequestMapping("/")
    public Map<String, Object> getHome() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start","시작페이지");

        return map;
    }
}
