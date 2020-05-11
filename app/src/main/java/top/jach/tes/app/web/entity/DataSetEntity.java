package top.jach.tes.app.web.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.domain.action.StatefulAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Table(name = "dataset")
@Entity
public class DataSetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long createdTime;

    private Long updatedTime;

    private String datapath;

}
