package com.mpole.imp.api.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Time;
import java.time.LocalDateTime;

@Getter @Setter
@Table("user_info")
public class UserInfo {

    @Id
    @Column("user_serial")
    private Integer userSerial;

    @Column("group_id")
    private Integer groupId;

    @Column("user_id")
    private String userId;

    @Column("user_pw")
    private String userPw;

    @Column("user_pw_type")
    private Integer userPwType;

    @Column("user_name")
    private String userName;

    @Column("user_authority")
    private Long userAuthority;

    @Column("user_level")
    private Integer userLevel;

    @Column("user_property")
    private String userProperty;

    @Column("ins_time")
    private LocalDateTime insTime;

    @Column("mod_time")
    private LocalDateTime modTime;
}
