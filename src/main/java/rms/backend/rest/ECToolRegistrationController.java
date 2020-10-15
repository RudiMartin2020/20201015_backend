package rms.backend.rest;

import org.springframework.web.bind.annotation.*;
import rms.backend.service.ECToolRegistrationService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ECToolRegistration")
public class ECToolRegistrationController {
    @Resource(name="ECToolRegistrationService")
    ECToolRegistrationService ecToolRegistrationService;

    @GetMapping("/getToolList")
    public Map<String,Object> getToolList(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        // List
        List<Map<String, Object>> list = ecToolRegistrationService.getToolList(reqText);
        // InLineEQPList
        List<Map<String, Object>> inLineEQPList = ecToolRegistrationService.getInLineEQPList(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", list);
        resultMap.put("inLineEQPList", inLineEQPList);
        resultMap.put("state", "OK");

        return resultMap;
    }

    @PostMapping("/saveTool")
    public Map<String,Object> saveTool(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        int ret = ecToolRegistrationService.saveTool(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", ret);
        resultMap.put("state", "OK");

        return resultMap;
    }

    @PostMapping("/getInLineEQPCheckList")
    public Map<String,Object> getInLineEQPCheckList(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        List<Map<String, Object>> list  = ecToolRegistrationService.getInLineEQPCheckList(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", list);
        resultMap.put("state", "OK");

        return resultMap;
    }

    @DeleteMapping("/deleteTool")
    public Map<String,Object> deleteTool(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        int ret = ecToolRegistrationService.deleteTool(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", ret);
        resultMap.put("state", "OK");

        return resultMap;
    }

}
