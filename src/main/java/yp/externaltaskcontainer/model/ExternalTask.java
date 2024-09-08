package yp.externaltaskcontainer.model;


import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalTask {
    private List<String> command;
}
