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
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @PostMapping("/publish")
    public R publish(ServletRequest request, @RequestBody Publish publish){
//            //确定发布类型，获取校验器
//            FormatValidator<Publish> validator = moduleProvider.getValidator(publish.getType());
//            //先进行校验
//            ValidateInfo validateInfo = validator.formatValidate(publish);
//            if (validateInfo.isValidation()){
//                throw new EmosException(validateInfo.getDescription());
//            }
            String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
            Long userId = jwtUtil.getUserId(requestToken);
             ArrayList<Map<String,String>> objects = new ArrayList<>();
           for (String img1:publish.getImgs()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("src",img1);
                objects.add(map);
            }
            publish.setImg(JSONObject.toJSONString(objects));
            publish.setPublisher(userId);
            publishService.createPublish(publish);
            return R.ok();

    }

    @PostMapping("/delPublish")
    public BaseGduiDTO<?> publish(Long id){
        try {
            publishService.deletePublish(id);
            return BaseGduiDTO.ok();
        }catch (Exception e){
            return BaseGduiDTO.error();
        }
    }

    @GetMapping("publish")
    public R PublishAll(){
        List<Publish> publishes = publishService.queryPublishAll();

        List<PublishVo> collect = publishes.stream().map(e -> publishMapper.entityToVo(e)).collect(Collectors.toList());
        return R.ok().put("data",collect);
    }

    @PostMapping("publishByType")
    public R PublishByType(@RequestBody Publish type){
        try {
            ModuleType moduleType = type.getType();
            List<Publish> publishes = publishService.queryPublishByType(moduleType);

            List<PublishVo> collect = publishes.stream().map(e -> publishMapper.entityToVo(e)).collect(Collectors.toList());
            return R.ok().put("data",collect);
        }catch (Exception e){
            return R.error();
        }
    }
    @PostMapping("publishBySearch")
    public R publishBySearch(@RequestBody PublishRequest publishRequest){
        try {
            List<Publish> publishes = publishService.queryPublishByContent(publishRequest.getSearchText());

            List<PublishVo> collect = publishes.stream().map(e -> publishMapper.entityToVo(e)).collect(Collectors.toList());
            return R.ok().put("data",collect);
        }catch (Exception e){
            return R.error("查询失败");
        }
    }

    @PostMapping("publishById")
    public R  PublishByType(@RequestBody Publish publishs, ServletRequest request){
        try {
            String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
            PublishVo publishVo = new PublishVo();
            if (requestToken != null){
                Long userId = jwtUtil.getUserId(requestToken);

                Publish publish = publishService.queryPublishById(publishs.getId());
                publishVo = publishMapper.entityToVo(publish);
                if (publish.getPublisher().equals(userId)){
                    publishVo.setMark(true);
                }else {
                    publish.setMark(false);
                }
            }else {
                Publish publish = publishService.queryPublishById(publishs.getId());
                publishVo = publishMapper.entityToVo(publish);
                publishVo.setMark(true);
            }
            HashMap<String, Object> map = new HashMap<>();

            map.put("imags", JSONObject.parseObject(publishVo.getImg(),List.class));
            ArrayList<Map<String,Object>> list = new ArrayList<>();
            Field[] fields = publishVo.getClass().getDeclaredFields();
            List<String> showList = Arrays.stream("title,content,startingTime,endOfTime,destination".split(","))
                    .collect(Collectors.toList());
            for (Field field : fields) {
                field.setAccessible(true); // 设置属性可访问
                try {
                    HashMap<String, Object> singMap = new HashMap<>();
                    if (showList.contains(field.getName())){
                        String name = null;
                        switch (field.getName()){
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
                                    name  = "结单时间";
                                    break;
                            case "destination":
                                    name  = "目的地";
                                    break;
                         }
                        singMap.put("label",name );
                        singMap.put("value", field.get(publishVo)); // 将属性名和属性值放到Map中
                    }
                    list.add(singMap);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            map.put("list",list);
            return R.ok().put("data",map);
        }catch (Exception e){
            return R.error();
        }
    }

//    @PostMapping("getOrderToMap")
//    public R getPublishToMap(@RequestBody Publish publish){
//        Publish publish1 = publishService.queryPublishById(publish.getId());
//        publish1
//    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public BaseGduiDTO<?> uploadImageFile(@RequestParam("img") MultipartFile uploadImage) throws Exception {

        String fileName = uploadImage.getOriginalFilename();
        String imgFilePath = "D:\\img";
        File targetFile = new File(imgFilePath, fileName);
        //保存
        uploadImage.transferTo(targetFile);
        return BaseGduiDTO.ok("https://39868bi104.yicp.fun/school/image?img="+fileName);
    }

    @GetMapping("/myPublish")
    public R myPublish(ServletRequest request){
        try {
            String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
            Long userId = jwtUtil.getUserId(requestToken);

            User user = new User();
            user.setId(userId);
            List<Publish> publishes = publishService.queryPublishByPublisher(user);
            List<PublishVo> collect = publishes.stream().map(e -> publishMapper.entityToVo(e)).collect(Collectors.toList());
            return R.ok().put("data",collect);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping("/image")
    public void image(HttpServletRequest request,HttpServletResponse response){
        String img = request.getParameter("img");
        InputStream is = null;
        OutputStream os = null;
        try {
            response.setContentType("image/jpeg");
            File file = new File("D:\\img\\"+img);
            response.addHeader("Content-Length", "" + file.length());
            is = new FileInputStream(file);
            os = response.getOutputStream();
            IOUtils.copy(is, os);
        } catch (Exception e) {
            log.error("下载图片发生异常", e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("关闭流发生异常", e);
            }
        }
    }


    public byte[] getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try { in = new FileInputStream("D:\\img\\"+imgFile);
            data = new byte[ in .available()]; in .read(data);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try { in .close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    /**
     * 获取请求头里面的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("token");
        }
        return token;

    }

//    public void getPersonPic(String picName, HttpServletResponse response) throws IOException {
//        ServletOutputStream outputStream = null;
//        try {
//            byte[] bytes = getPic(picName);
//            outputStream = response.getOutputStream();
//            outputStream.write(bytes);
//            outputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//            if (outputStream != null) {
//                outputStream.close();
//            }
//        }
//    }
//
//    public byte[] getPic(String picName) {
//        //进行图片路径处理。拼接上图片名
//        final Path path = Path.of(System.getProperty("user.dir"),"/home","/pic",picName);
//        byte[] bytes = null;
//        if (path.toFile().exists()) {
//            try {
//                bytes = Files.readAllBytes(path);
//            } catch (Exception e) {
//            }
//        }else {}
//        return bytes;
//    }
}
