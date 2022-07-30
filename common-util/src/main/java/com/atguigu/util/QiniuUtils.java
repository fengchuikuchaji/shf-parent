package com.atguigu.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 包名:com.atguigu.util
 *
 * @author Leevi
 * 日期2022-03-27  19:52
 */
public class QiniuUtils {
    private static String accessKey = "hP5yNAjiCwhqmaQgm_y1U3a-_aYXpUVLU8b78Vvc";
    private static String secretKey = "3QtbSyLxLXBiyhj7LKifrjsWN8qdBguAZvBZg_P3";
    private static String bucket = "leevishfxa";

    /**
     * 指定一个本地文件的路径，将改文件上传到七牛云存储中，并且指定文件上传到七牛云中的名字
     * @param filePath  本地文件路径
     * @param fileName  上传到七牛云中的文件名字
     */
    public static void upload2Qiniu(String filePath,String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(filePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 将文件的字节数组上传到七牛云中(形成一张图片)
     * @param bytes  上传文件对应的字节数组
     * @param fileName  上传到七牛云中的文件名字
     */
    public static void upload2Qiniu(byte[] bytes, String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 删除七牛云中的文件
     * @param fileName  要删除的七牛云中的文件名字
     */
    public static void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 获取七牛云中的文件的url，为了我们能够访问到七牛云中的文件，我们需要拼接一个url
     * @param uuidName
     * @return
     */
    public static String getUrl(String uuidName) {
        return "http://rdlos9bmy.hn-bkt.clouddn.com/" + uuidName;
    }
}
