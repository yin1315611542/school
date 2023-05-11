package com.yanda.school.controller;

import com.alibaba.fastjson.JSONObject;
import com.yanda.school.auth.JwtUtil;
import com.yanda.school.config.BaseGduiDTO;
import com.yanda.school.moudel.ModuleProvider;
import com.yanda.school.moudel.ModuleType;
import com.yanda.school.publish.Publish;
import com.yanda.school.publish.PublishRequest;
import com.yanda.school.publish.PublishVo;
import com.yanda.school.publish.mapper.PublishMapper;
import com.yanda.school.publish.service.PublishService;
import com.yanda.school.user.User;
import com.yanda.school.utils.R;
import com.yanda.school.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 发布模块控制层
 */
@RestController
@Slf4j
@RequestMapping("school")
public class PublishController {
    @Autowired
    PublishService publishService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ModuleProvider moduleProvider;
    @Autowired
    PublishMapper publishMapper;

    //创建订单接口
    @PostMapping("/publish")
    public R publish(ServletRequest request, @RequestBody Publish publish) {
        //            //确定发布类型，获取校验器
        //            FormatValidator<Publish> validator = moduleProvider.getValidator(publish.getType());
        //            //先进行校验
        //            ValidateInfo validateInfo = validator.formatValidate(publish);
        //            if (validateInfo.isValidation()){
        //                throw new EmosException(validateInfo.getDescription());
        //            }
        //从请求中拿出token
        String token = TokenUtil.getRequestToken((HttpServletRequest) request);
        //破解token，解析出当前登录用户id
        Long userId = jwtUtil.getUserId(token);
        ArrayList<Map<String, String>> objects = new ArrayList<>();
        //整理前端传过来的图片数据格式，转换成数据可存储的方式，方便查询时直接使用此格式
        for (String img1 : publish.getImgs()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("src", img1);
            objects.add(map);
        }
        //设置图片
        publish.setImg(JSONObject.toJSONString(objects));
        //设置发布者
        publish.setPublisher(userId);
        //创建单子
        publishService.createPublish(publish);
        return R.ok();
    }

    @PostMapping("/delPublish")
    public BaseGduiDTO<?> publish(Long id) {
        try {
            publishService.deletePublish(id);
            return BaseGduiDTO.ok();
        } catch (Exception e) {
            return BaseGduiDTO.error();
        }
    }

    //查询全部单子
    @GetMapping("publish")
    public R PublishAll(HttpServletRequest s) {
        //查询全部的单子
        List<Publish> publishes = publishService.queryPublishAll();
        //将字段转化为前端可用字段，与业务无关
        List<PublishVo> collect = publishes.stream().map(e -> publishMapper.entityToVo(e)).collect(Collectors.toList());
        return R.ok().put("data", collect);
    }

    //查询指定类型的发布单子
    @PostMapping("publishByType")
    public R PublishByType(@RequestBody Publish type) {
        try {
            //获取要查询的发布类型
            ModuleType moduleType = type.getType();
            List<Publish> publishes = publishService.queryPublishByType(moduleType);
            //将字段转化为前端可用字段，与业务无关
            List<PublishVo> collect = publishes.stream()
                    .map(e -> publishMapper.entityToVo(e))
                    .collect(Collectors.toList());
            return R.ok().put("data", collect);
        } catch (Exception e) {
            return R.error();
        }
    }
    //关键字搜索查询单子
    @PostMapping("publishBySearch")
    public R publishBySearch(@RequestBody PublishRequest publishRequest) {
        try {
            //按照关键字进行查询
            List<Publish> publishes = publishService.queryPublishByContent(publishRequest.getSearchText());

            List<PublishVo> collect = publishes.stream()
                    .map(e -> publishMapper.entityToVo(e))
                    .collect(Collectors.toList());
            return R.ok().put("data", collect);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }
    //查询某个订单的详情
    @PostMapping("publishById")
    public R PublishByType(@RequestBody Publish publishs, ServletRequest request) {
        try {
            String token = TokenUtil.getRequestToken((HttpServletRequest) request);
            PublishVo publishVo = new PublishVo();
            if (token != null) {
                //如果token不为空，从token中取出用户id进行查询
                Long userId = jwtUtil.getUserId(token);
                Publish publish = publishService.queryPublishById(publishs.getId());
                publishVo = publishMapper.entityToVo(publish);
                if (publish.getPublisher().equals(userId)) {
                    publishVo.setMark(true);
                } else {
                    publish.setMark(false);
                }
            } else {
                //如果token为空，使用前端传过来的用户id
                Publish publish = publishService.queryPublishById(publishs.getId());
                publishVo = publishMapper.entityToVo(publish);
                publishVo.setMark(true);
            }
            HashMap<String, Object> map = new HashMap<>();
            //获取数据库中存储的发布图片地址，转化成前端可已解析的形式
            map.put("imags", JSONObject.parseObject(publishVo.getImg(), List.class));
            ArrayList<Map<String, Object>> list = new ArrayList<>();
            //遍历发布订单的字段，选取前端需要展示的字段给前端
            Field[] fields = publishVo.getClass().getDeclaredFields();
            List<String> showList = Arrays.stream("title,content,startingTime,endOfTime,destination".split(","))
                    .collect(Collectors.toList());
            for (Field field : fields) {
                field.setAccessible(true); // 设置属性可访问
                try {
                    HashMap<String, Object> singMap = new HashMap<>();
                    if (showList.contains(field.getName())) {
                        String name = null;
                        switch (field.getName()) {
                            case "title":
                                name = "标题";
                                break;
                            case "content":
                                name = "备注";
                                break;
                            case "startingTime":
                                name = "开始时间";
                                break;
                            case "endOfTime":
                                name = "结单时间";
                                break;
                            case "destination":
                                name = "目的地";
                                break;
                        }
                        singMap.put("label", name);
                        singMap.put("value", field.get(publishVo)); // 将属性名和属性值放到Map中
                        list.add(singMap);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            map.put("list", list);
            return R.ok().put("data", map);
        } catch (Exception e) {
            return R.error();
        }
    }

    @GetMapping("getOrderToMap")
    public R getPublishToMap() {
        try {
            PublishVo publishVo = new PublishVo();
            Integer userId = 9;
            Publish publish = publishService.queryPublishById(442L);
            publishVo = publishMapper.entityToVo(publish);
            if (publish.getPublisher().equals(userId)) {
                publishVo.setMark(true);
            } else {
                publish.setMark(false);
            }

            HashMap<String, Object> map = new HashMap<>();

            map.put("imags", JSONObject.parseObject(publishVo.getImg(), List.class));
            ArrayList<Map<String, Object>> list = new ArrayList<>();
            Field[] fields = publishVo.getClass().getDeclaredFields();
            List<String> showList = Arrays.stream("title,content,startingTime,endOfTime,destination".split(","))
                    .collect(Collectors.toList());
            for (Field field : fields) {
                field.setAccessible(true); // 设置属性可访问
                try {
                    HashMap<String, Object> singMap = new HashMap<>();
                    if (showList.contains(field.getName())) {
                        String name = null;
                        switch (field.getName()) {
                            case "title":
                                name = "标题";
                                break;
                            case "content":
                                name = "备注";
                                break;
                            case "startingTime":
                                name = "开始时间";
                                break;
                            case "endOfTime":
                                name = "结单时间";
                                break;
                            case "destination":
                                name = "目的地";
                                break;
                        }
                        singMap.put("label", name);
                        singMap.put("value", field.get(publishVo)); // 将属性名和属性值放到Map中
                        list.add(singMap);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            map.put("list", list);
            return R.ok().put("data", map);
        } catch (Exception e) {
            return R.error();
        }
    }

    //查询我的发布接口
    @GetMapping("/myPublish")
    public R myPublish(ServletRequest request) {
        try {
            String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
            Long userId = jwtUtil.getUserId(requestToken);

            User user = new User();
            user.setId(userId);
            List<Publish> publishes = publishService.queryPublishByPublisher(user);
            List<PublishVo> collect = publishes.stream()
                    .map(e -> publishMapper.entityToVo(e))
                    .collect(Collectors.toList());
            return R.ok().put("data", collect);
        } catch (Exception e) {
            return R.error();
        }
    }

//    @PostMapping("/uploadImage")
//    @ResponseBody
//    public BaseGduiDTO<?> uploadImageFile(@RequestParam("img") MultipartFile uploadImage) throws Exception {
//
//        String fileName = uploadImage.getOriginalFilename();
//        String imgFilePath = "D:\\img";
//        File targetFile = new File(imgFilePath, fileName);
//        //保存
//        uploadImage.transferTo(targetFile);
//        return BaseGduiDTO.ok("https://39868bi104.yicp.fun/school/image?img=" + fileName);
//    }
//
//    @RequestMapping("/image")
//    public void image(HttpServletRequest request, HttpServletResponse response) {
//        String img = request.getParameter("img");
//        InputStream is = null;
//        OutputStream os = null;
//        try {
//            response.setContentType("image/jpeg");
//            File file = new File("D:\\img\\" + img);
//            response.addHeader("Content-Length", "" + file.length());
//            is = new FileInputStream(file);
//            os = response.getOutputStream();
//            IOUtils.copy(is, os);
//        } catch (Exception e) {
//            log.error("下载图片发生异常", e);
//        } finally {
//            try {
//                if (os != null) {
//                    os.flush();
//                    os.close();
//                }
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException e) {
//                log.error("关闭流发生异常", e);
//            }
//        }
//    }
//
//    public byte[] getImgStr(String imgFile) {
//        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//        InputStream in = null;
//        byte[] data = null;
//        // 读取图片字节数组
//        try {
//            in = new FileInputStream("D:\\img\\" + imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return data;
//    }
//
//    /**
//     * 获取请求头里面的token
//     */
//    private String getRequestToken(HttpServletRequest httpRequest) {
//        //从header中获取token
//        String token = httpRequest.getHeader("token");
//        //如果header中不存在token，则从参数中获取token
//        if (StringUtils.isBlank(token)) {
//            token = httpRequest.getParameter("token");
//        }
//        return token;
//    }
}
