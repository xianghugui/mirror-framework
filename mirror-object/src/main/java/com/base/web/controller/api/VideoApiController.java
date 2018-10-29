package com.base.web.controller.api;

import com.afdUtils.*;
import com.afdUtils.utils.BufferInfo;
import com.afdUtils.utils.ImageLoader;
import com.base.web.bean.*;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.bean.po.resource.Resources;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import com.base.web.service.resource.FileService;
import com.base.web.service.resource.ResourcesService;
import com.base.web.util.OSSUtils;
import com.base.web.util.ProcessUtils;
import com.base.web.util.ResourceUtil;
import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;
import io.swagger.annotations.*;
import org.hsweb.commons.DateTimeUtils;
import org.hsweb.commons.MD5;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static com.base.web.core.message.ResponseMessage.ok;

/**
 * 硬件端上传视频相关接口类
 * 2018-04-01
 */
@Api(value = "VideoApiController", description = "视频相关接口操作类")
@RequestMapping("/api/video")
@RestController
public class VideoApiController {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private FileService fileService;
    @Resource
    private DeviceService deviceService;

    @Resource
    private VideoService videoService;
    @Resource
    private ResourcesService resourcesService;
    @Resource
    private SpecificationService specificationService;
    @Resource
    private VideoUserService videoUserService;
    @Resource
    private VideoOrderService videoOrderService;
    @Resource
    private FileRefService fileRefService;
    @Resource
    private FaceFeatureService faceFeatureService;

    @Resource
    private ShopDeviceService shopDeviceService;

    @Resource
    private OSSUtils ossUtils;

    private static final Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
    /**
     * 虹软人脸识别
     */
    private static final String APPID = isWin ? "GaNub6zqMbUnpmfBkM9BNJzvCHF8atej4mSjD5a5rzbg" : "GaNub6zqMbUnpmfBkM9BNJzo2syzZUHfef2nPzQpnQKh";
    private static final String FD_SDKKEY = isWin ? "6RuX1NkZyFCsjVDsynZaCpKFBXWdeeTXoGJsCYERaYWi" : "5n7iBGp5wqVyJqSwq1WjGmvdAqcJ1bpEBBVWH83LZw2M";
    private static final String FR_SDKKEY = isWin ? "6RuX1NkZyFCsjVDsynZaCpKjq8ZLi8XGyK5AdKijT6og" : "5n7iBGp5wqVyJqSwq1WjGmw7pSezPmb6DDvUhStyjLiC";

    private static final int FD_WORKBUF_SIZE = 20 * 1024 * 1024;
    private static final int FR_WORKBUF_SIZE = 40 * 1024 * 1024;
    private static final int MAX_FACE_NUM = 50;
    private static final boolean bUseBGRToEngine = true;
    private Pointer hFREngine;
    private Pointer hFDEngine;

    /**
     * 保存临时视频并顺时旋转90°
     * @param multipartFile
     */
    private File videoRotate(MultipartFile multipartFile) throws InterruptedException, IOException, TimeoutException {
        //设置临时路径
        String absPath = fileService.getFileBasePath().concat("/video/").concat(multipartFile.getOriginalFilename());
        File path = new File(absPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String newName = MD5.encode(String.valueOf(System.nanoTime())); //临时文件名 ,纳秒的md5值
        String fileAbsName = absPath.concat("/").concat(newName).concat(".mp4");
        //保存文件
        fileService.getFileLength(multipartFile.getInputStream(), fileAbsName, 0);
        File file = new File(fileAbsName);
        //旋转视频
        List<String> convert = new ArrayList();
        convert.add("ffmpeg");
        convert.add("-i");
        convert.add(fileAbsName);
        convert.add("-metadata:s:v");
        convert.add("rotate=270");
        convert.add("-codec");
        convert.add("copy");
        convert.add("-y");
        convert.add(fileAbsName + ".mp4");
        ProcessUtils.executeCommand(convert);
        return file;
    }


    @ApiOperation(value = "硬件端上传用户试衣视频接口", notes = "硬件端上传用户试衣视频接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Long", name = "deviceID", value = "上传视频设备id", required = true),
                    @ApiImplicitParam(paramType = "body", dataType = "MultipartFile[]", name = "files", value = "上传视频文件及其正脸图片文件,文件数组，一组只能上传一个视频和一张图片", required = true)})
    @RequestMapping(value = "/uploadVideoFile/{deviceUserName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseMessage uploadVideoFile(
            @RequestParam("file") MultipartFile[] files,
            @PathVariable("deviceUserName") String deviceUserName) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("start upload , file number:%s", files.length));
        }
        List<Resources> resourcesList = new LinkedList<>();
//        byte[] data = new byte[0];
        //保存到数据库人脸特征值
        Map<Integer, byte[]> map = new HashMap();
        if (files.length > 0) {
            String FILE_NAME = "";
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    if (logger.isInfoEnabled()) {
                        logger.info("start write file:{}", file.getOriginalFilename());
                    }
                    String fileName = file.getOriginalFilename();
                    fileService.saveFile(file.getInputStream(), fileName);
                    //上传到OSS
                    File videoFile = videoRotate(file);
                    Resources resources = ossUtils.uploadFile(videoFile);
                    videoFile.delete();
                    String resourcesName = resources.getName();
                    String resourcesType = getMimeType(resourcesName);
                    if ("image".equals(resourcesType)) {
                        //提取人脸特征值
                        String currentImagePath;
                        if (isWin) {
                            currentImagePath = System.getProperty("user.dir") + File.separator
                                    + "upload" + File.separator + "face" + File.separator + file.getOriginalFilename();
                        } else {
                            currentImagePath = "/data/apache-tomcat-8.5.31/bin/upload/face/" + file.getOriginalFilename();
                        }
                        //用户拍照的特征值
                        File faceFile = new File(currentImagePath);
                        file.transferTo(faceFile);
                        if (extractFace(faceFile) == null) {

                        } else {
                            //重置特征值数组大小
//                            data = new byte[extractFace(faceFile).length];
                            for (int j = 0; j < extractFace(faceFile).length; j++) {
                                if (extractFace(faceFile)[j] != null) {
                                    map.put(j, extractFace(faceFile)[j].toByteArray());
                                }
                            }
                        }
                        resources.setType("0");
                        faceFile.delete();
                    } else {
                        resources.setType("1");
                        //添加视频信息
                        FILE_NAME = fileName.substring(0, fileName.lastIndexOf("."));
                    }
                    resourcesList.add(resources);
                }
            }//响应上传成功的资源信息
            Device device = deviceService.createQuery().where(Device.Property.username, deviceUserName).single();
            ShopDevice shopDevice = shopDeviceService
                    .createQuery()
                    .where(ShopDevice.Property.deviceId, device.getId()).single();
            Video video = new Video();
            video.setId(GenericPo.createUID());
            video.setDeviceId(device.getId());
            video.setRecordId(GenericPo.createUID());
            video.setDeviceShopId(shopDevice.getShopId());
            video.setStatus(0);
            video.setCreateTime(new Date());
            video.setVideoName(FILE_NAME);
            videoService.insert(video);
            //向视频对应图片人脸组特征表里插入每个人脸特征值
            for (byte[] faceFeature : map.values()) {
                FaceFeature faceFeature1 = new FaceFeature();
                faceFeature1.setId(GenericPo.createUID());
                faceFeature1.setVideoId(video.getId());
                faceFeature1.setFaceFeature(faceFeature);
                faceFeatureService.insert(faceFeature1);
            }
            for (Resources resources : resourcesList) {
                String type = resources.getType();
                FileRef fileRef = new FileRef();
                fileRef.setId(GenericPo.createUID());
                fileRef.setRefId(video.getRecordId());
                fileRef.setResourceId(resources.getId());
                fileRef.setPriority(0);
                if ("0".equals(type)) {
                    fileRef.setType(0);
                } else {
                    fileRef.setType(1);
                }
                fileRefService.insert(fileRef);
            }
            return ResponseMessage.ok(resourcesList)
                    .include(Resources.class, "id", "name", "md5", "type");
        } else {
            return ResponseMessage.error("上传文件不存在");
        }
    }

    /**
     * 判断资源类型
     *
     * @param fileName
     * @return
     */
    private static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileName);
        if (type == null) {
            type = "";
        } else {
            type = "image";
        }
        return type;
    }

    //------------------face match-----------------------------
    @ApiOperation(value = "用户上传个人照片，人脸检测返回视频组接口", notes = "List<Map>中Map数据：" +
            "<br>{" +
            "<br>Long videoId 视频id ;" +
            "<br>Date createTime 创建时间; " +
            "<br>Long recordId 资源关联id ;" +
            "<br>String shopName 店铺名称 ;" +
            "<br>String address 店铺地址; " +
            "<br>String videoImg 视频图片地址" +
            "<br>}", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "body", dataType = "MultipartFile", name = "file", value = "用户用于人脸识别的个人正脸照", required = true)})
    @RequestMapping(value = "/face/faceMatch", method = RequestMethod.POST)
    //此处需要做事物操作处理，（暂时不做事物处理，时间允许再做修改）
//    @Transactional
    public ResponseMessage faceRecognize(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {
        String currentImagePath;
        if (isWin) {
            currentImagePath = System.getProperty("user.dir") + File.separator
                    + "upload" + File.separator + "face" + File.separator + file.getOriginalFilename();
        } else {
            currentImagePath = "/data/apache-tomcat-8.5.31/bin/upload/face/" + file.getOriginalFilename();
        }
        //用户拍照的特征值
        File faceFile = new File(currentImagePath);
        file.transferTo(faceFile);

        //单个人脸特征值数组
        AFR_FSDK_FACEMODEL[] faceFeatureB = extractFace(faceFile);
        //上传的检测图片没有检测到人脸
        //获取数据库的特征值
        List<Map> videoList;
        if (faceFeatureB == null) {
            //上传文件没有检测到人脸直接返回空数组
            videoList = null;
        } else {
            //获取数据库的特征值
            videoList = videoUserService.selectNoBelongToVideoInfo();

            for (int i = 0; i < videoList.size(); ) {
                //查询当前视频对应图片包含的所有人脸特征值
                List<FaceFeature> faceFeatureList = faceFeatureService.createQuery()
                        .where(FaceFeature.Property.videoId, videoList.get(i).get("videoId"))
                        .list();
                if (faceFeatureList.size() == 0) {
                    videoList.remove(i);
                } else {
                    for (int k = 0; k < faceFeatureList.size(); k++) {
                        AFR_FSDK_FACEMODEL faceFeatureA = AFR_FSDK_FACEMODEL.fromByteArray(faceFeatureList.get(k).getFaceFeature());
                        //检测成功之后跳出当前寻缓
                        if (compareFaceSimilarity(hFREngine, faceFeatureA, faceFeatureB[0]) - 0.63 > 0) {
                            FileRef fileRef = fileRefService.createQuery().where(FileRef.Property.refId, videoList.get(i).get("recordId"))
                                    .and(FileRef.Property.type, 0).single();
                            videoList.get(i).put("videoImg", ossUtils.selectVideoImageUrl(String.valueOf(fileRef.getRefId())));
                            i++;
                            continue;
                        } else if (k + 1 == faceFeatureList.size()) {//匹配失败，从未检测列表中移除当前检测数据
                            videoList.remove(i);
                        }
                    }
                }
            }
        }
        return ok(videoList);
    }


//--------------------------------------------------------------------------------------


    @ApiOperation(value = "用户从人脸识别视频组中选择视频进行关联", notes = "视频id数组 id Long 类型")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            value = {@ApiImplicitParam(paramType = "List<Long>", dataType = "List<Long>",
                    name = "selectIds", value = "用户选择视频组"),
            })
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseMessage selectedVideoUpload(@RequestBody List<String> selectIds) {
        // 将移动端人脸识别后，选择的视频保存到服务器端
        logger.info("selected ids: {}", selectIds);
        if (selectIds.size() > 0) {
            for (int i = 0; i < selectIds.size(); i++) {
                Video video = videoService.selectByPk(Long.valueOf(selectIds.get(i)));
                if (video == null) {
                    return ResponseMessage.error("参数错误");
                }
                VideoUser videoUser = new VideoUser();
                videoUser.setId(GenericPo.createUID());
                videoUser.setShopId(video.getDeviceShopId());
                videoUser.setCreateTime(new Date());
                videoUser.setStatus(0);
                videoUser.setUserId(WebUtil.getLoginUser().getId());
                videoUser.setVideoId(video.getId());
                videoUserService.insert(videoUser);
            }
        }
        return ResponseMessage.ok(1);
    }
// --------------------------------------------------------------------------------


    @ApiOperation(value = "用户删除自己的试衣视频", notes = "根据视频id删除")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Long",
                    name = "videoId", value = "视频id", required = true),
            })
    @RequestMapping(value = "/{videoId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AccessLogger("删除")
    public ResponseMessage deleteUserVideo(@PathVariable("videoId") Long videoId) {
        VideoUser old = videoUserService.createQuery()
                .where(VideoUser.Property.videoId, videoId)
                .and(VideoUser.Property.userId, WebUtil.getLoginUser().getId())
                .single();
        old.setStatus(99);
        videoUserService.saveOrUpdate(old);
        return ok("删除成功");
    }

    // --------------------------------------------------------------------------------


    @ApiOperation(value = "用户删除自己的试衣秀", notes = "根据视频id删除")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Long",
                    name = "videoId", value = "视频id", required = true),
            })
    @RequestMapping(value = "/videoshow/{videoId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AccessLogger("删除试衣秀")
    public ResponseMessage deleteUserVideoShow(@PathVariable("videoId") Long videoId) {
        videoUserService.resetGoodsId(videoId, WebUtil.getLoginUser().getId());
        return ok("删除成功");
    }


    // --------------------------------------------------------------------------------

    @ApiOperation(value = "试衣库首页分页加载用户试衣视频", notes = "List<Map>中Map数据：" +
            "<br>{" +
            "<br>Long videoId 视频id ;" +
            "<br>Date createTime 创建时间;" +
            "<br>String videoUrl 视频地址 ;" +
            "<br>String videoImageUrl 视频图片地址 ;" +
            "<br>String shopName 店铺名称 " +
            "<br>}", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryUserVideo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryUserVideo(QueryParam queryParam, HttpServletRequest req) {
        PagerResult<Map> object = videoUserService.userVideoList(queryParam, req);
        return ResponseMessage.ok(object);
    }

    // --------------------------------------------------------------------------------

    @ApiOperation(value = "视频询价时服尺码查询", notes = "视频询价时服尺码查询", response = Specification.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryGoodsSpec", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryGoodsSpecList() {
        List<Specification> list = specificationService.createQuery().where(Specification.Property.type, 0).list();
        return ok(list);
    }


    // --------------------------------------------------------------------------------
    @ApiOperation(value = "根据视频id用户分享自己试衣视频", notes = "根据视频id用户分享自己试衣视频" +
            "返回map" +
            "<br>{" +
            "<br>Long videoId 视频id ;" +
            "<br>String videoUrl 视频地址 ;" +
            "<br>String shopName 店铺名称 " +
            "<br>}")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/shareVideo/{videoId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage shareVideo(@PathVariable("videoId") Long videoId, HttpServletRequest req) {
        Map map = videoUserService.shareVideo(videoId, req);
        return ok(map);
    }

    // --------------------------------------------------------------------------------
    @RequestMapping(value = "/video/{videoName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AccessLogger("获取视频相关信息")
    public ResponseMessage video(@PathVariable("videoName") String videoName, HttpServletRequest req) {
        List<Map> video = resourcesService.video(videoName);
        logger.info("data default: {}", video);
        for (Map map : video) {
            map.put("md5", ResourceUtil.resourceBuildPath(req, String.valueOf(map.get("uId")).trim(), ".mp4"));
            map.put("imgSrc", ResourceUtil.resourceBuildPath(req, String.valueOf(map.get("imgSrc")).trim()));
        }
        return ok(video);
    }

    // --------------------------------------------------------------------------------
    @ApiOperation(value = "根据视频id查询当前视频是否已经咨询过价格", notes = "根据视频id查询当前视频是否已经咨询过价格" +
            "返回" +
            "<br>{" +
            "<br>200，已询价， 201 未询价" +

            "<br>}")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Long",
                    name = "videoId", value = "视频id", required = true),
            })
    @RequestMapping(value = "/queryVideoIfConsultPrice/{videoId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryVideoIfConsultPrice(@PathVariable("videoId") Long videoId) {
        VideoOrder videoOrder = videoOrderService.createQuery().where(VideoOrder.Property.videoId, videoId).single();
        if (videoOrder != null) {
            return ResponseMessage.ok(200);
        } else {
            return ResponseMessage.ok(201);
        }
    }

    public VideoApiController() {
        // init Engine
        Pointer pFDWorkMem = CLibrary.INSTANCE.malloc(FD_WORKBUF_SIZE);
        Pointer pFRWorkMem = CLibrary.INSTANCE.malloc(FR_WORKBUF_SIZE);

        PointerByReference phFDEngine = new PointerByReference();
        NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_InitialFaceEngine(APPID, FD_SDKKEY, pFDWorkMem, FD_WORKBUF_SIZE, phFDEngine, _AFD_FSDK_OrientPriority.AFD_FSDK_OPF_0_HIGHER_EXT, 32, MAX_FACE_NUM);
        if (ret.longValue() != 0) {
            CLibrary.INSTANCE.free(pFDWorkMem);
            CLibrary.INSTANCE.free(pFRWorkMem);
            System.out.println(String.format("AFD_FSDK_InitialFaceEngine ret 0x%x", ret.longValue()));
            throw new RuntimeException();
        }

        // print FDEngine version
        hFDEngine = phFDEngine.getValue();

        PointerByReference phFREngine = new PointerByReference();
        ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_InitialEngine(APPID, FR_SDKKEY, pFRWorkMem, FR_WORKBUF_SIZE, phFREngine);
        if (ret.longValue() != 0) {
            AFD_FSDKLibrary.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
            CLibrary.INSTANCE.free(pFDWorkMem);
            CLibrary.INSTANCE.free(pFRWorkMem);
            System.out.println(String.format("AFR_FSDK_InitialEngine ret 0x%x", ret.longValue()));
            throw new RuntimeException();
        }
        hFREngine = phFREngine.getValue();
    }

    /**
     * 传入图片返回byte[]人脸特征值
     *
     * @param file
     * @return AFR_FSDK_FACEMODEL
     */
    public AFR_FSDK_FACEMODEL[] extractFace(File file) {
        // load Image Data
        ASVLOFFSCREEN inputImg = loadImage(file);
        FaceInfo[] faceInfos = doFaceDetection(hFDEngine, inputImg);
        if (faceInfos.length < 1) {
            return null;
        }
        //获取图片里面能检测到的所有人脸
        AFR_FSDK_FACEINPUT[] faceinput = new AFR_FSDK_FACEINPUT[faceInfos.length];
        for (int i = 0; i < faceInfos.length; i++) {
            faceinput[i] = new AFR_FSDK_FACEINPUT();
            faceinput[i].lOrient = faceInfos[i].orient;
            faceinput[i].rcFace.left = faceInfos[i].left;
            faceinput[i].rcFace.top = faceInfos[i].top;
            faceinput[i].rcFace.right = faceInfos[i].right;
            faceinput[i].rcFace.bottom = faceInfos[i].bottom;
        }

        //获取图片里面能检测到的所有人脸特征值
        AFR_FSDK_FACEMODEL[] faceFeature = new AFR_FSDK_FACEMODEL[faceinput.length];
        for (int i = 0; i < faceinput.length; i++) {
            faceFeature[i] = new AFR_FSDK_FACEMODEL();
            NativeLong nativeLong = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_ExtractFRFeature(hFREngine, inputImg, faceinput[i], faceFeature[i]);
            if (nativeLong.longValue() != 0) {
                faceFeature[i] = null;//人脸特征值获取失败将当前人脸特征值置空
            }
        }
        return faceFeature;
    }

    /**
     * 人脸检测数组
     * 检测图片中存在的人脸
     *
     * @param hFDEngine hFD引擎
     * @param inputImg  待检测人人脸土拍你
     * @return
     */
    public FaceInfo[] doFaceDetection(Pointer hFDEngine, ASVLOFFSCREEN inputImg) {
        FaceInfo[] faceInfo = new FaceInfo[0];

        PointerByReference ppFaceRes = new PointerByReference();
        NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_StillImageFaceDetection(hFDEngine, inputImg, ppFaceRes);
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFD_FSDK_StillImageFaceDetection ret 0x%x", ret.longValue()));
            return faceInfo;
        }

        AFD_FSDK_FACERES faceRes = new AFD_FSDK_FACERES(ppFaceRes.getValue());
        if (faceRes.nFace > 0) {
//            faceInfo = new FaceInfo[faceRes.nFace];
            faceInfo = new FaceInfo[1];
            int area;
            for (int i = 0; i < faceRes.nFace; i++) {
                MRECT rect = new MRECT(new Pointer(Pointer.nativeValue(faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
                int orient = faceRes.lfaceOrient.getPointer().getInt(i * 4);
                area = (rect.right - rect.left) * (rect.bottom - rect.top);
                if (i == 0) {
                    faceInfo[0] = new FaceInfo();
                    faceInfo[0].left = rect.left;
                    faceInfo[0].top = rect.top;
                    faceInfo[0].right = rect.right;
                    faceInfo[0].bottom = rect.bottom;
                    faceInfo[0].orient = orient;
                    faceInfo[0].area = area;
                } else {
                    if (faceInfo[0].area < area) {
                        faceInfo[0].left = rect.left;
                        faceInfo[0].top = rect.top;
                        faceInfo[0].right = rect.right;
                        faceInfo[0].bottom = rect.bottom;
                        faceInfo[0].orient = orient;
                        faceInfo[0].area = area;
                    } else {
                        continue;
                    }
                }
            }
        }
        return faceInfo;
    }

    //提供特征值获取分数
    public float compareFaceSimilarity(Pointer hFREngine, AFR_FSDK_FACEMODEL faceFeatureA, AFR_FSDK_FACEMODEL faceFeatureB) {
        // calc similarity between faceA and faceB
        FloatByReference fSimilScore = new FloatByReference(0.0f);
        NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, faceFeatureA, faceFeatureB, fSimilScore);
        faceFeatureA.freeUnmanaged();
        faceFeatureB.freeUnmanaged();
        if (ret.longValue() != 0) {
            return 0.0f;
        }
        System.out.println("人脸相似度为：");
        System.out.println(fSimilScore.getValue());
        return fSimilScore.getValue();
    }


    public ASVLOFFSCREEN loadImage(File file) {
        ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

        if (bUseBGRToEngine) {
            BufferInfo bufferInfo = ImageLoader.getBGRFromFile(file);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else {
            BufferInfo bufferInfo = ImageLoader.getI420FromFile(file);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
            inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2, inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[3] = Pointer.NULL;
        }

        inputImg.setAutoRead(false);
        return inputImg;
    }
}
