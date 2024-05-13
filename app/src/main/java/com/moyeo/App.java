package com.moyeo;

import com.moyeo.service.RecruitBoardService;
import com.moyeo.service.RecruitMemberService;
import com.moyeo.service.ReviewBoardService;
import com.moyeo.vo.RecruitBoard;
import com.moyeo.vo.RecruitMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@SpringBootApplication
@EnableTransactionManagement
@PropertySource({
    "classpath:config/ncp.properties",
    "classpath:config/ncp-secret.properties"
})
@Controller
public class App {

    private static final Log log = LogFactory.getLog(App.class);
    private final ReviewBoardService reviewBoardService;
    private final RecruitBoardService recruitBoardService;
    private final RecruitMemberService recruitMemberService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/home")
    public void home(Model model) {

        List<RecruitBoard> list = recruitBoardService.findByCurrentByLimit6();

        for(int i=0; i<list.size(); i++){
            List<RecruitMember> recruitMembers = recruitMemberService.allApplicant(list.get(i).getRecruitBoardId());
            list.get(i).setApplicants(recruitMembers);
        }

        model.addAttribute("listByLimit3", reviewBoardService.findByCreatedDateByLimit3());
        model.addAttribute("listByLikeCountLimit3", reviewBoardService.findByLikeCountByLimit3());
        model.addAttribute("listByViewsLimit3", reviewBoardService.findByViewsByLimit3());
        model.addAttribute("listByCurrentLimit6", list);

    }


    @GetMapping("/homedemo2")
    public void homedemo2() {

    @GetMapping("/originalhome")
    public void newhome(Model model) {

        List<RecruitBoard> list = recruitBoardService.findByCurrentByLimit6();

        for(int i=0; i<list.size(); i++){
            List<RecruitMember> recruitMembers = recruitMemberService.allApplicant(list.get(i).getRecruitBoardId());
            list.get(i).setApplicants(recruitMembers);
        }

        model.addAttribute("listByLimit3", reviewBoardService.findByCreatedDateByLimit3());
        model.addAttribute("listByLikeCountLimit3", reviewBoardService.findByLikeCountByLimit3());
        model.addAttribute("listByViewsLimit3", reviewBoardService.findByViewsByLimit3());
        model.addAttribute("listByCurrentLimit6", list);

    }
}
