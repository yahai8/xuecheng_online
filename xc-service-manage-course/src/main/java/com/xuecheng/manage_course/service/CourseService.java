package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CoursePublishResult;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: MuYaHai
 * Date: 2019/11/28, Time: 21:22
 */
@Service
public class CourseService {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    CmsPageClient cmsPageClient;
    @Autowired
    CoursePubRepository coursePubRepository;
    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    //查询课程计划
    public TeachplanNode findTeacherList(String courseid) {
        return teachplanMapper.findTeachplanList(courseid);
    }

    //添加课程计划
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //判断是否为空，抛出异常
        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        //得到课程id
        String courseid = teachplan.getCourseid();
        //得到父路径
        String parentid = teachplan.getParentid();
        //如果父目录id为空，就跟据courseId去查询父目录id
        if (StringUtils.isEmpty(parentid)) {
            //查询父目录id
            parentid = this.getTeachplanRoot(courseid);
        }
        //拿到根目录id再去查询是否存在
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }
        Teachplan teachplanParent = optional.get();
        //得到层级
        String parentGrade = teachplanParent.getGrade();
        //设置设置parentid，给要添加的teachplan
        teachplan.setParentid(parentid);
        if (parentGrade != null) {
            //将层级设为当前层级+1
            Integer value = Integer.valueOf(parentGrade);
            ++value;
            teachplan.setGrade(String.valueOf(value));
        }
        //设置添加当前课程计划的课程id
        teachplan.setCourseid(teachplanParent.getCourseid());
        //保存
        teachplanRepository.save(teachplan);
        //返回成功
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //获取根节点id，如果没有根节点就添加根节点
    public String getTeachplanRoot(String courseid) {
        //根据课程id查看是否有根节点
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        //检验课程是否存在,不存在直接返回
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        //存在课程，查看是否有根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        //没有就新增节点，当前节点为根节点
        if (teachplanList == null || teachplanList.size() <= 0) {
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseid);
            teachplanRoot.setPname(courseBase.getName());
            //根节点
            teachplanRoot.setParentid("0");
            //层级为1
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");
            teachplanRepository.save(teachplanRoot);
            teachplanList.add(teachplanRoot);
            //返回根节点id
            return teachplanRoot.getId();
        }
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }

    //分页查询课程
    public QueryResponseResult findCourseListPage(String companyId,int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        //企业id
        courseListRequest.setCompanyId(companyId);
        if (page <= 0) {
            page = 0;
        }
        if (size <= 0) {
            size=20;
        }
        //分页设置
        PageHelper.startPage(page, size);
        //分页结果
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        if (courseListPage != null) {
            //得到分页后的信息
            List<CourseInfo> courseInfoList = courseListPage.getResult();
            //得到总记录数
            long total = courseListPage.getTotal();
            QueryResult queryResult = new QueryResult();
            //设置查询结果
            queryResult.setList(courseInfoList);
            //设置记录数
            queryResult.setTotal(total);
            return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        }
        return new QueryResponseResult(CommonCode.FAIL, null);
    }

    //添加课程基础
    @Transactional
    public ResponseResult add(CourseBase courseBase) {
        if (courseBase != null) {
            courseBase.setMt("1-3");
            courseBase.setSt("1-3-2");
            courseBaseRepository.save(courseBase);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //修改课程基础信息
    @Transactional
    public ResponseResult updateCoursebase(String id, CourseBase courseBase) {
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseResult(CommonCode.FAIL);
        }
        CourseBase base = optional.get();
        if (StringUtils.isEmpty(courseBase.getMt())) {
            courseBase.setMt(base.getMt());
        }
        if (StringUtils.isEmpty(courseBase.getSt())) {
            courseBase.setSt(base.getSt());
        }
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //根据id查询课程信息
    public CourseBase getCoursebaseById(String id) {
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_ISNULL);
        }
        return optional.get();
    }

    //查询课程营销
    public CourseMarket getCourseMarketById(String id) {
        Optional<CourseMarket> optionalCourseMarket = courseMarketRepository.findById(id);
        if (!optionalCourseMarket.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_MARKET_ISNULL);
        }
        CourseMarket courseMarket = optionalCourseMarket.get();
        return courseMarket;
    }

    //更新课程营销信息
    @Transactional
    public ResponseResult updateCourseMarket(String courseid, CourseMarket courseMarket) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseid);
        if (!optional.isPresent()) {
            this.addCourseMarket(courseid, courseMarket);
        }
        courseMarketRepository.save(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //新增课程营销
    @Transactional
    public CourseMarket addCourseMarket(String coursrid, CourseMarket courseMarket) {
        Optional<CourseBase> optional = courseBaseRepository.findById(coursrid);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_ISNULL);
        }
        CourseBase courseBase = optional.get();
        courseMarket.setId(courseBase.getId());
        return courseMarketRepository.save(courseMarket);
    }

    //上传图片的课程id和图片相对路径地址
    public ResponseResult addCoursePic(String courseId, String pic) {
        //根据id查询是否已有图片
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        //不为空就修改
        if (optional.isPresent()) {
            coursePic = optional.get();
        }
        //为空就创建
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询图片信息
    public CoursePic findCoursePic(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            CoursePic coursePic = optional.get();
            return coursePic;
        }
        return null;
    }

    //删除图片信息
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //课程视图查询
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            courseView.setCoursePic(coursePic);
        }
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        TeachplanNode teachplanNode = teachplanMapper.findTeachplanList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    //课程预览
    public CoursePublishResult preview(String courseId) {
        CourseBase courseBase = this.getCoursebaseById(courseId);

        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        //远程请求cms保存页面信息
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        String pageId = cmsPageResult.getCmsPage().getPageId();
        String pageUrl = previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //课程发布
    public CoursePublishResult publish(String courseId) {
        //课程信息
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //发布课程详情页面
        CmsPostPageResult cmsPostPageResult = this.publish_page(courseId);
        if (!cmsPostPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //更新课程状态
        CourseBase courseBase1 = this.saveCoursePubState(courseId);
        //创建课程索引
        CoursePub coursePub = this.createCoursePub(courseId);
        //向数据库保存索引信息
        CoursePub newCoursePub = this.saveCoursePub(courseId, coursePub);
        if (newCoursePub == null) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_INDEX_ERROR);
        }
        //保存媒资信息
        this.saveTeachplanMediaPub(courseId);
        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //更新课程状态
    public CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }

    //查询课程基础信息
    public CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_BASE_ISNULL);
        }
        return optional.get();
    }

    //发布课程正式页面
    public CmsPostPageResult publish_page(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //课程预览站点
        cmsPage.setSiteId(publish_siteId);
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        //发布页面
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        return cmsPostPageResult;
    }

    //保存saveCoursePub
    public CoursePub saveCoursePub(String id, CoursePub coursePub) {
        if (StringUtils.isNotEmpty(id)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        //创建一个CoursePub对象
        CoursePub coursePubNew = null;
        //根据id查询coursePub
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(id);
        if (coursePubOptional.isPresent()) {
            coursePubNew = coursePubOptional.get();
        }
        if (coursePubNew == null) {
            coursePubNew = new CoursePub();
        }
        //将提交过来的数据拷贝到新的coursePub中
        BeanUtils.copyProperties(coursePub, coursePubNew);
        //设置主键
        coursePubNew.setId(id);
        //更新时间戳为最新的
        coursePubNew.setTimestamp(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }

    //创建coursePub
    public CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        coursePub.setId(id);

        //查询课程基础信息，courseBase
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            BeanUtils.copyProperties(courseBase, coursePub);
        }

        //查询课程图片
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            BeanUtils.copyProperties(coursePic, coursePub);
        }

        //课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        //课程计划
        TeachplanNode teachplanList = teachplanMapper.findTeachplanList(id);
        //将课程计划转成json
        String json = JSON.toJSONString(teachplanList);
        coursePub.setTeachplan(json);
        return coursePub;
    }

    //保存媒资信息
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null) {
            ExceptionCast.cast(CommonCode.INVALIDPARAM);
        }

        String teachplanId = teachplanMedia.getTeachplanId();
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(teachplanId);
        if (!teachplanOptional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_ISNULL);
        }
        Teachplan teachplan = teachplanOptional.get();
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade) || !"3".equals(grade)) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        TeachplanMedia one = null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        if (!teachplanMediaOptional.isPresent()) {
            one = new TeachplanMedia();
        } else {
            one = teachplanMediaOptional.get();
        }
        //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //保存媒资信息
    public void saveTeachplanMediaPub(String courseId) {
        //查询媒资信息
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        //删除
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia, teachplanMediaPub);
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        teachplanMediaPubRepository.saveAll(teachplanMediaPubList);
    }
}