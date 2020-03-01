package com.henu.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lv
 * @date 2020-03-01 17:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DLXMessage implements Serializable {

    private static final long serialVersionUID = -8716397999181727961L;

    private String exchange;

    private String queueName;

    private String content;

    private long times;

    public DLXMessage(String queueName, String content, long times){
        super();
        this.queueName = queueName;
        this.content = content;
        this.times = times;
    }

}
