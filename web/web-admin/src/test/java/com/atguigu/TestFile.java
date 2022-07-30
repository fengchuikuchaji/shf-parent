package com.atguigu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * 包名:com.atguigu
 *
 * @author Leevi
 * 日期2022-06-17  11:06
 */
public class TestFile {
    @Test
    public void testUploadFile(){
        //测试使用七牛云上传文件
        //构造一个带指定 Zone 对象的配置类对象
        //Zone.zone2() 表示华南区
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        //创建文件上传管理器对象
        UploadManager uploadManager = new UploadManager(cfg);

        //生成上传凭证，然后准备上传
        //你自己的七牛账号的accessKey
        String accessKey = "hP5yNAjiCwhqmaQgm_y1U3a-_aYXpUVLU8b78Vvc";
        //你自己的七牛账号的secretKey
        String secretKey = "3QtbSyLxLXBiyhj7LKifrjsWN8qdBguAZvBZg_P3";
        //你自己创建的存储空间的名称
        String bucket = "leevishfxa";

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        //你需要上传到七牛云的那张照片的路径
        String localFilePath = "D:\\qiniu\\test.jpg";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //指定文件上传到七牛云之后，用什么名字存储到七牛云，如果不指定名字，那么七牛云会自动生成一个名字(就是该文件的hash值)
        String key = null;

        //鉴权对象
        Auth auth = Auth.create(accessKey, secretKey);
        //创建出文件上传时候token
        String upToken = auth.uploadToken(bucket);

        try {
            //真正执行文件上传，并且获取到响应结果
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //打印上传文件在七牛云中的名字
            System.out.println(putRet.key);
            //打印上传文件的hash值
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            //如果出现异常，打印异常信息
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
    public void testDeleteFromQiniu(){
        //构造一个带指定 Zone 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());

        //你自己的七牛账号的accessKey
        String accessKey = "hP5yNAjiCwhqmaQgm_y1U3a-_aYXpUVLU8b78Vvc";
        //你自己的七牛账号的secretKey
        String secretKey = "3QtbSyLxLXBiyhj7LKifrjsWN8qdBguAZvBZg_P3";
        //你自己创建的存储空间的名称
        String bucket = "leevishfxa";

        //要删除的那个文件在七牛云中的路径(名字)
        String key = "Foj-TSvRKZUIbmpBLkJh2AVJb1Jr";

        //鉴权对象
        Auth auth = Auth.create(accessKey, secretKey);
        //创建控件管理器对象
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //删除空间中的文件
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
