package application.model.viewmodel.point;

import application.model.viewmodel.common.LayoutHeaderVM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointLanding {
    private LayoutHeaderVM layoutHeaderVM;
    private PointVM pointVM;
    private int flag;
}
