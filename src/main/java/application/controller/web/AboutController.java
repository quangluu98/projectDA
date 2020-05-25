package application.controller.web;

import application.model.viewmodel.about.AboutLanding;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController extends BaseController {
    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request) {
        AboutLanding vm = new AboutLanding();
        vm.setLayoutHeaderVM(super.getLayoutHeaderVM(request));
        model.addAttribute("vm", vm);
        return "about";
    }
}
