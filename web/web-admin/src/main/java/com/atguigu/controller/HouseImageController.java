package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-17  14:02
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    private static final String PAGE_UPLOAD_SHOW = "house/upload";
    private static final String DETAIL_ACTION = "redirect:/house/";
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;
    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type,
                             Model model) {
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return PAGE_UPLOAD_SHOW;
    }

    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type,
                         @RequestParam("file") MultipartFile[] files) throws IOException {
        //1. 遍历上传的文件files
        for (int i = 0; i < files.length; i++) {
            //1.1 将图片上传到七牛云
            //1.1.1 获取客户端上传的文件的文件名
            MultipartFile file = files[i];
            String originalFilename = file.getOriginalFilename();
            //1.1.2 获取唯一的文件名
            String uuidName = FileUtil.getUUIDName(originalFilename);
            //1.1.3 上传文件到七牛云
            QiniuUtils.upload2Qiniu(file.getBytes(),uuidName);
            //1.1.4 获取该文件在七牛云中的url
            String url = QiniuUtils.getUrl(uuidName);

            //1.2 将七牛云的图片url保存到数据库表(hse_house_image)
            //1.2.1 创建一个HouseImage对象,并且设置相关的属性
            HouseImage houseImage = new HouseImage();
            houseImage.setHouseId(houseId);
            houseImage.setImageName(uuidName);
            houseImage.setImageUrl(url);
            houseImage.setType(type);
            //1.2.2 调用业务层方法,将houseImage保存到数据库
            houseImageService.insert(houseImage);
            if (i == 0){
                //1.3 判断当前房源是否有默认图片，如果没有，则可以将当前上传的第一张图片设置为默认图片
                //1.3.1 根据houseId查询房源信息
                House house = houseService.getById(houseId);
                //1.3.2 判断house是否有defaultImageUrl属性
                if (house.getDefaultImageUrl() == null || "".equals(house.getDefaultImageUrl()) || "null".equals(house.getDefaultImageUrl())) {
                    //1.3.3 将当前上传的第一张图片设置为默认图片
                    house.setDefaultImageUrl(url);
                    //1.3.4 更新数据库中的房源信息
                    houseService.update(house);
                }
            }
        }

        //2. 返回Result.ok()表示上传成功
        return Result.ok();
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,
                         @PathVariable("id") Long id) {
        //1. 根据图片的id查询图片信息
        HouseImage houseImage = houseImageService.getById(id);
        //2. 从七牛云中删除图片
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //3. 从数据库中删除图片
        houseImageService.delete(id);

        //4. 判断当前删除的图片是否是房源的默认图片，如果是，则修改房源的默认图片地址为null
        House house = houseService.getById(houseId);
        //删除房源图片的时候，判断当前删除的图片是否是房源的默认图片，如果是，则修改房源的默认图片地址为null
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())) {
            //则将当前房源的默认图片地址设置为null
            house.setDefaultImageUrl("null");
            //更新数据库中的房源信息
            houseService.update(house);
        }
        //5. 重定向显示房源详情页
        return DETAIL_ACTION + houseId;
    }
}
