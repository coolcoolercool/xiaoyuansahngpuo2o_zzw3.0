package com.zzw.o2o.web.superadmin;

import com.zzw.o2o.entity.Area;
import com.zzw.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: zzw5005
 * date: 2018/10/25 10:13
 */

@Controller
@RequestMapping("/superadmin")
public class AreaController {
    /*Logger logger = LoggerFactory.getLogger(AreaController.class);*/

    @Autowired
    private AreaService areaService;

    @RequestMapping(value="/listarea", method = RequestMethod.GET)
    @ResponseBody //这里将model转换成json格式传递给前端
    private Map<String,Object> listArea(){
        /*logger.info("===start===");*/
        long startTime = System.currentTimeMillis();

        Map<String, Object> modelMap = new HashMap<>();
        List<Area> list = new ArrayList<>();
        try {
            list = areaService.getAreaList();
            modelMap.put("rows",list);         //这里的rows和total是可以根据前端需要更改，由于项目使用的easyUI所以这里使用这两个
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        /*logger.error("test error!");

        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime-startTime);

        logger.info("===end===");*/
        return modelMap;
    }




}
