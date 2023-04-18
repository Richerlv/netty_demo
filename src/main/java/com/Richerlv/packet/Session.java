package com.Richerlv.packet;

import lombok.Data;

/**
 * @author lvyanling
 * @date 2023-04-18
 */

@Data
public class Session {
    // 用户唯一性标识
    private String userId;
    private String userName;
}
