package rms.backend.rest;

import org.springframework.web.bind.annotation.*;
import rms.backend.service.ECIDService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ECID")
public class ECIDController {
    @Resource(name="ECIDService")
    ECIDService ecIDService;

    @GetMapping("/getECIDList")
    public Map<String,Object> getECIDList(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        // List
        List<Map<String, Object>> list = ecIDService.getECIDList(reqText);
        // rmsValueTypeList
        List<Map<String, Object>> rmsValueTypeCodeList = ecIDService.getCodeList("{\"CATEGORY\":\"RMS_VALUE_TYPE\"}");
        // rmsUnitTypeList
        List<Map<String, Object>> rmsUnitTypeCodeList = ecIDService.getCodeList("{\"CATEGORY\":\"RMS_UNIT_TYPE\"}");
        rmsUnitTypeCodeList.removeIf(p -> p.get("CODE").equals("TIME"));
        rmsUnitTypeCodeList.removeIf(p -> p.get("CODE").equals("RECIPE"));
        // rmsSpecOptionList
        List<Map<String, Object>> rmsSpecOptionCodeList = ecIDService.getCodeList("{\"CATEGORY\":\"RMS_SPEC_OPTION\"}");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", list);
        resultMap.put("rmsValueTypeCodeList", rmsValueTypeCodeList);
        resultMap.put("rmsUnitTypeCodeList", rmsUnitTypeCodeList);
        resultMap.put("rmsSpecOptionCodeList", rmsSpecOptionCodeList);
        resultMap.put("state", "OK");

        return resultMap;
    }

    @PostMapping("/saveECID")
    public Map<String,Object> saveECID(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        int ret = ecIDService.saveECID(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", ret);
        resultMap.put("state", "OK");

        return resultMap;
    }

    @DeleteMapping("/deleteECID")
    public Map<String,Object> deleteECID(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        int ret = ecIDService.deleteECID(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", ret);
        resultMap.put("state", "OK");

        return resultMap;
    }

}
