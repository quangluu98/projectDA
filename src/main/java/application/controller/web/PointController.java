package application.controller.web;

import application.data.entity.User;
import application.data.service.UserService;
import application.model.viewmodel.point.PointLanding;
import application.model.viewmodel.point.PointVM;
import application.model.viewmodel.user.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/point")
public class PointController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public String point(Model model, HttpServletRequest request,
                        @Valid @ModelAttribute("point") UserVM point) {

        PointLanding vm = new PointLanding();
        if(point.getEmail() != null && !point.getEmail().isEmpty()) {
            User user = userService.findUserByEmail(point.getEmail().trim());
            if(user != null) {
                vm.setFlag(1);
                PointVM pointVM = new PointVM();
                pointVM.setPoint(user.getPoint());
                pointVM.setUserName(user.getUserName());
                vm.setPointVM(pointVM);
            } else {
                vm.setFlag(2);
            }
        }

        vm.setLayoutHeaderVM(super.getLayoutHeaderVM(request));

        model.addAttribute("vm", vm);
        return "point";
    }
}
