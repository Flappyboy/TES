package top.jach.tes.plugin.jhkt.maintain.size;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
@Setter
@Getter
public class AllVersionMicroServices {
    private List<Microservices> allVMicroservices = new ArrayList<>();
}
