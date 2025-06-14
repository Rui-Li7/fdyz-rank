package me.rui.fdyzrank;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "fdyz-rank")
public class FdyzRankConfig {
    private String answer;
    private String adminKey;
    private long adminId;
    private String updateKey;
    private int classCount;
}
