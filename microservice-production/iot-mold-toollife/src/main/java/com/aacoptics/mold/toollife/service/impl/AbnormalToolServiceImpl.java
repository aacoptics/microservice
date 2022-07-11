package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.dao.AbnormalToolMapper;
import com.aacoptics.mold.toollife.entity.AbnormalTool;
import com.aacoptics.mold.toollife.entity.ProgramDetail;
import com.aacoptics.mold.toollife.entity.ScrapedTool;
import com.aacoptics.mold.toollife.provider.EmailProvider;
import com.aacoptics.mold.toollife.service.AbnormalToolService;
import com.aacoptics.mold.toollife.service.ProgramDetailService;
import com.aacoptics.mold.toollife.service.ScrapedToolService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Slf4j
public class AbnormalToolServiceImpl extends ServiceImpl<AbnormalToolMapper, AbnormalTool> implements AbnormalToolService {

    @Autowired
    ScrapedToolService scrapedToolService;

    @Autowired
    AbnormalToolMapper abnormalToolMapper;

    @Autowired
    ProgramDetailService programDetailService;

    @Autowired
    EmailProvider emailProvider;

    public void saveAbnormalTool() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN.withHour(7).withMinute(30)).plusDays(-1);
        List<ScrapedTool> scrapedToolList = scrapedToolService.getScrapedList(startDateTime.format(df));
        for (ScrapedTool scrapedTool : scrapedToolList) {
            String _toolNo = scrapedTool.getCodeNo();
            AbnormalTool abnormalTool = new AbnormalTool();
            abnormalTool.setToolNo(_toolNo);
            abnormalTool.setMatCode(scrapedTool.getMatCode());
            abnormalTool.setMatName(scrapedTool.getMatName());
            abnormalTool.setLifeSalvage(scrapedTool.getLifeSalvage());
            abnormalTool.setScrapedTime(LocalDateTime.parse(scrapedTool.getCreateDate().substring(0, 19), df));
            ProgramDetail programDetail = programDetailService.getAbnormalTotalTime(_toolNo);
            if (programDetail == null)
                continue;
            abnormalTool.setRealLifeSalvage(programDetail.getTotalTime().toString());
            abnormalTool.setLastMachineNo(programDetail.getMachineNo());
            abnormalTool.setArea(programDetail.getProgramName());

            if (programDetail.getTotalTime() < Integer.parseInt(scrapedTool.getLifeSalvage())) {
                this.save(abnormalTool);
            }
        }
    }

    @Override
    public List<AbnormalTool> getAbnormalTools() {
        QueryWrapper<AbnormalTool> wrapper = new QueryWrapper<>();
        wrapper.eq("is_confirmed", 0)
                .orderByDesc("scraped_time");
        List<AbnormalTool> abnormalToolList = list(wrapper);
        Iterator iterator = abnormalToolList.iterator();
        while(iterator.hasNext()) {
            AbnormalTool abnormalTool = (AbnormalTool) iterator.next();
            String lifeSalvage = abnormalTool.getLifeSalvage();
            String realLifeSalvage = abnormalTool.getRealLifeSalvage();
            if(StringUtils.isNotBlank(lifeSalvage) && StringUtils.isNotBlank(realLifeSalvage)) {
                double lifeSalvageNumber = Double.parseDouble(lifeSalvage);
                double realLifeSalvageNumber = Double.parseDouble(realLifeSalvage);
                double lifeRateNumber = realLifeSalvageNumber/lifeSalvageNumber*100;
                if(lifeRateNumber > 90) {
                    iterator.remove();
                } else {
                    DecimalFormat df = new DecimalFormat("0.00");
                    String lifeRate = df.format(lifeRateNumber);
                    abnormalTool.setLifeRate(lifeRate + "%");
                }
            }
        }
        return abnormalToolList;
    }

    @Override
    public void updateAbnormalReason(AbnormalTool abnormalTool) {
        abnormalTool.setCheckedTime(LocalDateTime.now());
        this.updateById(abnormalTool);
    }

    @Override
    public void updateConfirmInfo(AbnormalTool abnormalTool) {
        abnormalTool.setConfirmedTime(LocalDateTime.now());
        this.updateById(abnormalTool);
    }

    @Override
    public Integer getAbnormalCount(String startTime, String endTime) {
        QueryWrapper<AbnormalTool> wrapper = new QueryWrapper<>();
        wrapper.between("scraped_time", startTime, endTime);
        return abnormalToolMapper.selectCount(wrapper);
    }

    @Override
    public void sendAbnormalEmail() {
        List<String> emailList = abnormalToolMapper.getAbnormalEmailList();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN.withHour(7).withMinute(30)).plusDays(-2);
        LocalDateTime endDateTime = startDateTime.plusDays(1);
        QueryWrapper<AbnormalTool> wrapper = new QueryWrapper<>();
        wrapper.between("scraped_time", startDateTime, endDateTime)
                .eq("is_confirmed", 1);
        List<AbnormalTool> abnormalTools = list(wrapper);
        Integer totalCount = getAbnormalCount(startDateTime.format(df), endDateTime.format(df));
        List<AbnormalTool> resultList = getRandomList(5, abnormalTools);
        String subject = "刀具寿命每日提醒";
        StringBuilder content = new StringBuilder("<html><head></head><body><h4>刀具寿命异常确认结果:</h4>");
        content.append("<h5>时间段：").append(startDateTime.format(df1)).append("至").append(endDateTime.format(df1)).append("</h5>");
        content.append("<h5>异常总数：" + totalCount + "</h5>");
        content.append("<h5>已确认数：" + abnormalTools.size() + "</h5>");
        if (resultList.size() > 0) {
            content.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=11px;;font-size:14px;\">");
            content.append("<tr style=\"background-color: #428BCA; color:#ffffff\">" +
                    "<th>刀具编号</th>" +
                    "<th>刀具物料号</th>" +
                    "<th>刀具名称</th>" +
                    "<th>标准寿命</th>" +
                    "<th>实际寿命</th>" +
                    "<th>报废时间</th>" +
                    "<th>原因</th></tr>");
            for (AbnormalTool data : resultList) {
                content.append("<tr>");
                content.append("<td>" + data.getToolNo() + "</td>");
                content.append("<td>" + data.getMatCode() + "</td>");
                content.append("<td>" + data.getMatName() + "</td>");
                content.append("<td>" + data.getLifeSalvage() + "</td>");
                content.append("<td>" + data.getRealLifeSalvage() + "</td>");
                content.append("<td>" + data.getScrapedTime().format(df1) + "</td>");
                content.append("<td>" + data.getReason() + "</td>");
                content.append("</tr>");
            }
            content.append("</table>");
        }
        content.append("<h6>来自模具刀具寿命</h6>");
        content.append("</body></html>");
        Map<String, Object> test = new HashMap<>();
        test.put("to", emailList);
        test.put("subject", subject);
        test.put("emailContent", content);
        emailProvider.sendEmail(test);
    }

    private List<AbnormalTool> getRandomList(Integer listNum, List<AbnormalTool> abnormalTools) {
        if (listNum == null) {
            listNum = 10;
        }
        int returnNum = abnormalTools.size();
        if (listNum < abnormalTools.size()) {
            returnNum = listNum;
        }
        int randomIndex = (int) (Math.random() * (abnormalTools.size() - returnNum));
        List<AbnormalTool> resultList = new ArrayList<>();
        for (int i = randomIndex; i < randomIndex + returnNum; i++) {
            resultList.add(abnormalTools.get(i));
        }
        return resultList;
    }
}