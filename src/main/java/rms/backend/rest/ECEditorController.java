package rms.backend.rest;

import org.springframework.web.bind.annotation.*;
import rms.backend.service.ECEditorService;
import rms.backend.service.ECIDService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ECEditor")
public class ECEditorController {
    @Resource(name="ECEditorService")
    ECEditorService ecEditorService;
    @Resource(name="ECIDService")
    ECIDService ecIDService;

    @GetMapping("/getECSPECList")
    public Map<String,Object> getECSPECList(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        // List
        List<Map<String, Object>> list = ecEditorService.getECSPECList(reqText);

        // rmsValueTypeList
        List<Map<String, Object>> rmsValueTypeCodeList = ecIDService.getCodeList("{\"CATEGORY\":\"RMS_VALUE_TYPE\"}");
        // rmsUnitTypeList
        List<Map<String, Object>> rmsUnitTypeCodeList = ecIDService.getCodeList("{\"CATEGORY\":\"RMS_UNIT_TYPE\"}");
        rmsUnitTypeCodeList.removeIf(p -> p.get("CODE").equals("TIME"));
        rmsUnitTypeCodeList.removeIf(p -> p.get("CODE").equals("RECIPE"));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", list);
        resultMap.put("rmsValueTypeCodeList", rmsValueTypeCodeList);
        resultMap.put("rmsUnitTypeCodeList", rmsUnitTypeCodeList);
        resultMap.put("state", "OK");

        return resultMap;
    }

    @PostMapping("/saveECSPEC")
    public Map<String,Object> saveECSPEC(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        int ret = ecEditorService.saveECSPEC(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", ret);
        resultMap.put("state", "OK");

        return resultMap;
    }

    @DeleteMapping("/deleteECSPEC")
    public Map<String,Object> deleteECSPEC(@RequestParam(value = "reqText", required = false) String reqText) throws Exception {

        int ret = ecEditorService.deleteECSPEC(reqText);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", ret);
        resultMap.put("state", "OK");

        return resultMap;
    }

}
